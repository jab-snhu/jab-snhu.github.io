/*
 * EventsViewModel.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.viewmodel;

import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.repo.EventRepository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The view model for the EventsActivity. Connects to the
 * {@link EventRepository} to fetch the user's events to display
 * or delete an existing event.
 */
public class EventsViewModel extends AndroidViewModel {
   private final EventRepository repo;

   /**
    * Interface for a callback after and event has been deleted.
    */
   public interface DeleteCallback { void onDelete(); }

   /**
    * EventsViewModel constructor. Initializes the event repository.
    *
    * @param application The application context.
    */
   public EventsViewModel(@NonNull Application application) {
      super(application);
      repo = new EventRepository(application);
   }

   /**
    * Fetches the {@link LiveData} list of the user's future events.
    *
    * @param userId The id for the user whose events should be fetched.
    * @return LiveData list of the specified user's future events.
    */
   public LiveData<List<Event>> userEvents(String userId) {
      return repo.userEvents(userId);
   }

   /**
    * Deletes the event matching the specified event id.
    *
    * @param eventId The id of the event to be deleted.
    * @param callback Callback triggered after the event has been deleted.
    */
   public void deleteEvent(long eventId, @NonNull DeleteCallback callback) {
      repo.delete(eventId, rows -> callback.onDelete());
   }
}
