/*
 * AddEditEventViewModel.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.viewmodel;

import com.jeffblagg.eventtracker.authentication.AuthManager;
import com.jeffblagg.eventtracker.authentication.FirebaseAuthManager;
import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.repo.EventRepository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * The view model for the AddEditEventActivity. Connects to the
 * {@link EventRepository} to add new events or save changes to existing
 * ones.
 */
public class AddEditEventViewModel extends AndroidViewModel {
   private final EventRepository repo;
   private final AuthManager authManager;

   /**
    * Interface for a callback after an event has been loaded.
    */
   public interface LoadEventCallback { void onEventLoaded(Event event); }

   /**
    * Interface for a callback after an event has been saved.
    */
   public interface SaveEventCallback { void onEventSaved(String id); }

   /**
    * Interface for a callback when encountering an error with an operation.
    */
   public interface ErrorCallback { void onError(String errorMessage); }

   /**
    * AddEditEventViewModel constructor. Initializes the event repository.
    *
    * @param application The application context.
    */
   public AddEditEventViewModel(@NonNull Application application) {
      super(application);
      repo = new EventRepository(application);
      authManager = new FirebaseAuthManager();
   }

   /**
    * Fetches an existing event matching the provided id.
    *
    * @param eventId The id of the event to fetch.
    * @param callback The callback returning the fetched event.
    */
   public void loadEvent(String eventId, LoadEventCallback callback) {
      repo.getEvent(eventId, callback::onEventLoaded);
   }

   /**
    * Creates a new event for the specified user.
    *
    * @param title The title of the event.
    * @param description The description of the event.
    * @param eventTime The time of the event, in milliseconds.
    * @param cardColor The color for the event card.
    * @param callback The callback triggered on successful save.
    * @param errorCallback The callback triggered if an error is encountered.
    */
   public void createNewEvent(String title,
                              String description,
                              long eventTime,
                              Integer cardColor,
                              SaveEventCallback callback,
                              ErrorCallback errorCallback) {
      String userId = authManager.getCurrentUserId();

      if (userId == null) {
          errorCallback.onError("No user is logged in.");
          return;
      }

      if (title.isBlank()) {
         errorCallback.onError("Title is required.");
         return;
      }

      // uses `null` for the event id because Firestore will provide an id
      // when adding the event
      Event event = new Event(null, userId, eventTime, title, description, cardColor);
      repo.add(event, callback::onEventSaved);
   }

   /**
    * Updates an existing event.
    *
    * @param event The event to update.
    * @param callback Callback triggered on successful update.
    */
   public void updateEvent(Event event, SaveEventCallback callback) {
      repo.update(event, callback::onEventSaved);
   }
}
