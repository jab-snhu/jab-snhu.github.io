/*
 * EventRepository.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.repo;

import com.jeffblagg.eventtracker.EventTrackerApp;
import com.jeffblagg.eventtracker.database.EventTrackerDatabase;
import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.reminder.EventReminderManager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link Event} Repository class that facilitates Event DAO operations
 * for the rest of the app.
 */
public class EventRepository {
    private final Application application;
    private final EventTrackerDatabase db;

    /**
     * Single thread background executor for Room calls.
     */
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    /**
     * Main thread handler to return to the main thread after background operations.
     */
    private final Handler mainLooper = new Handler(Looper.getMainLooper());

    /**
     * Interface for a callback after an event has been loaded.
     */
    public interface OnEventLoaded { void onResult(Event event); }

    /**
     * Interface for a callback after an event has been added.
     */
    public interface OnEventAdded { void onResult(long eventId); }

    /**
     * Interface for a callback after an event has been updated.
     */
    public interface OnEventUpdated { void onResult(int rows); }

    /**
     * Interface for a callback after an event has been deleted.
     */
    public interface onEventDeleted { void onResult(int rows); }

    /**
     * EventRepository constructor.
     *
     * @param application The application object used to access the database.
     */
    public EventRepository(Application application) {
        this.application = application;
        db = ((EventTrackerApp) application).getDatabase();
    }

    /**
     * Fetches a single event with the provided id.
     *
     * @param eventId The id of the event to fetch.
     * @param callback Action receiving the fetched event.
     */
    public void getEvent(long eventId, OnEventLoaded callback) {
        databaseExecutor.execute(() -> {
            Event event = db.eventDao().getEvent(eventId);
            mainLooper.post(() -> callback.onResult(event));
        });
    }

    /**
     * Creates a {@link LiveData} list of a user's upcoming events.
     *
     * @param userId The id of the user whose events are fetched.
     * @return LiveData list of the specified user's future events.
     */
    public LiveData<List<Event>> userEvents(long userId) {
        return db.eventDao().getEventsForUser(userId);
    }

    /**
     * Adds a new event and schedules the SMS event reminder.
     *
     * @param event The event to add.
     * @param callback Action receiving the added event's id.
     */
    public void add(Event event, OnEventAdded callback) {
        databaseExecutor.execute(() -> {
            long eventId = db.eventDao().insert(event);
            if (eventId > 0) {
                // schedule a reminder after event successfully added
                scheduleReminder(event);
            }
            mainLooper.post(() -> callback.onResult(eventId));
        });
    }

    /**
     * Updates an existing event. Cancels and reschedules the event reminder.
     *
     * @param event The event to update.
     * @param callback Action receiving the row value of the updated event.
     */
    public void update(Event event, OnEventUpdated callback) {
        databaseExecutor.execute(() -> {
            int rows = db.eventDao().update(event);
            if (rows > 0) {
                // cancel any existing event and reschedule in case the time changed.
                EventReminderManager.cancel(application, event.id);
                scheduleReminder(event);
            }
            mainLooper.post(() -> callback.onResult(rows));
        });
    }

    /**
     * Deletes an event and cancel any associated scheduled reminder.
     *
     * @param eventId The id of the event to delete.
     * @param callback Action receiving the row value of the deleted event.
     */
    public void delete(long eventId, onEventDeleted callback) {
        databaseExecutor.execute(() -> {
            db.eventDao().deleteEvent(eventId);
            EventReminderManager.cancel(application, eventId);
            mainLooper.post(() -> callback.onResult(1));
        });
    }

    /**
     * Schedules an event reminder to be sent two hours before the event time.
     *
     * @param event The event to schedule a reminder for.
     */
    private void scheduleReminder(Event event) {
        long twoHoursBeforeEventTime = event.eventTime - 2 * 60 * 60 * 100L;

        String startTimeMessage = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                .format(new Date(event.eventTime));
        String reminderMessage = "Friendly Reminder: " + event.title + " is starting at " + startTimeMessage;
        EventReminderManager.schedule(application, event.id, twoHoursBeforeEventTime, reminderMessage);
    }
}
