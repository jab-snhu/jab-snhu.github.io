/*
 * EventRepository.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.repo;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jeffblagg.eventtracker.authentication.AuthManager;
import com.jeffblagg.eventtracker.authentication.FirebaseAuthManager;
import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.reminder.EventReminderManager;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link Event} Repository class that facilitates Firestore Event operations
 * for the rest of the app.
 */
public class EventRepository {
    private final Application application;
    private final FirebaseFirestore firestore;
    private final AuthManager authManager;

    /**
     * Interface for a callback after an event has been loaded.
     */
    public interface OnEventLoaded { void onResult(Event event); }

    /**
     * Interface for a callback after an event has been added.
     */
    public interface OnEventAdded { void onResult(String eventId); }

    /**
     * Interface for a callback after an event has been updated.
     */
    public interface OnEventUpdated { void onResult(String eventId); }

    /**
     * Interface for a callback after an event has been deleted.
     */
    public interface onEventDeleted { void onResult(); }

    /**
     * EventRepository constructor.
     *
     * @param application The application object used to access the database.
     */
    public EventRepository(Application application) {
        this.application = application;
        this.firestore = FirebaseFirestore.getInstance();
        this.authManager = new FirebaseAuthManager();
    }

    /**
     * Fetches a single event with the provided id.
     *
     * @param eventId The id of the event to fetch.
     * @param callback Action receiving the fetched event.
     */
    public void getEvent(String eventId, OnEventLoaded callback) {
        String userId = authManager.getCurrentUserId();

        if (userId == null || eventId == null) {
            callback.onResult(null);
            return;
        }

        // Fetch the event from Firestore
        firestore.collection("users").document(userId)
                .collection("events").document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = document.toObject(Event.class);
                            callback.onResult(event);
                        } else {
                            callback.onResult(null);
                        }
                    } else {
                        callback.onResult(null);
                    }
                });
    }

    /**
     * Creates a {@link LiveData} list of a user's upcoming events.
     *
     * @param userId The id of the user whose events are fetched.
     * @return LiveData list of the specified user's future events.
     */
    public LiveData<List<Event>> userEvents(String userId) {
        MutableLiveData<List<Event>> eventsList = new MutableLiveData<>();

        // if no user is logged in, return an empty list
        if (userId == null) {
            eventsList.setValue(new ArrayList<>());
            return eventsList;
        }

        // Get future events from Firestore, ordered by time
        firestore.collection("users").document(userId)
                .collection("events")
                .whereGreaterThanOrEqualTo("eventTime", System.currentTimeMillis())
                .orderBy("eventTime", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    List<Event> events = new ArrayList<>();

                    // if there is no error and there are event snapshots, add them
                    // to the list
                    if (error == null && snapshots != null) {
                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            Event event = document.toObject(Event.class);
                            if (event != null) {
                                events.add(event);
                            }
                        }
                    }

                    eventsList.setValue(events);
                });

        return eventsList;
    }

    /**
     * Adds a new event and schedules the SMS event reminder.
     *
     * @param event The event to add.
     * @param callback Action receiving the added event's id.
     */
    public void add(Event event, OnEventAdded callback) {
        String userId = authManager.getCurrentUserId();

        if (userId == null) {
            callback.onResult(null);
            return;
        }

        // Get a reference to the user's events collection
        CollectionReference eventsCollection = firestore.collection("users")
                .document(userId)
                .collection("events");

        // Have Firestore generate an event id and set it for the event
        String eventId = eventsCollection.document().getId();
        event.id = eventId;

        // Add the new event
        eventsCollection.document(eventId).set(event)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        scheduleReminder(event);
                        callback.onResult(eventId);
                    } else {
                        // return a null id on failure
                        callback.onResult(null);
                    }
                });
    }

    /**
     * Updates an existing event. Cancels and reschedules the event reminder.
     *
     * @param event The event to update.
     * @param callback Action receiving the updated event's id.
     */
    public void update(Event event, OnEventUpdated callback) {
        String userId = authManager.getCurrentUserId();

        if (userId == null || event.id == null) {
            callback.onResult(null);
            return;
        }

        firestore.collection("users").document(userId)
                .collection("events").document(event.id)
                .set(event)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Cancel and reschedule reminder in case the event changed
                        EventReminderManager.cancel(application, event.id);
                        scheduleReminder(event);
                        callback.onResult(event.id);
                    } else {
                        // return a null id if the update failed
                        callback.onResult(null);
                    }
                });
    }

    /**
     * Deletes an event and cancel any associated scheduled reminder.
     *
     * @param eventId The id of the event to delete.
     * @param callback Action triggered after deletion.
     */
    public void delete(String eventId, onEventDeleted callback) {
        String userId = authManager.getCurrentUserId();

        if (userId == null || eventId == null) {
            callback.onResult();
            return;
        }

        // Delete the document from Firestore
        firestore.collection("users").document(userId)
                .collection("events").document(eventId)
                .delete()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       // Cancel the reminder after successful deletion
                       EventReminderManager.cancel(application, eventId);
                   }
                   callback.onResult();
                });
    }

    /**
     * Schedules an event reminder to be sent two hours before the event time.
     *
     * @param event The event to schedule a reminder for.
     */
    private void scheduleReminder(Event event) {
        long twoHoursBeforeEventTime = event.eventTime - 2 * 60 * 60 * 1000L;

        String startTimeMessage = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                .format(new Date(event.eventTime));
        String reminderMessage = "Friendly Reminder: " + event.title + " is starting at " + startTimeMessage;
        EventReminderManager.schedule(application, event.id, twoHoursBeforeEventTime, reminderMessage);
    }
}
