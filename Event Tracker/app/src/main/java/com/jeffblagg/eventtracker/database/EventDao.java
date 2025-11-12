/*
 * EventDao.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.database;

import com.jeffblagg.eventtracker.entities.Event;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface used by Room to handle
 * database operations on the {@link Event} table.
 */
@Dao
public interface EventDao {

    /**
     * Inserts a new {@link Event} into the database, or replaces an existing
     * event with the same id.
     *
     * @param event The event to insert.
     * @return The id of the inserted event.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Event event);

    /**
     * Updates an existing {@link Event} in the database.
     *
     * @param event The event to update.
     * @return The number of rows updated.
     */
    @Update
    int update(Event event);

    /**
     * Fetches all future events for a given user, ordered chronologically.
     * <p>NOTE: Only events scheduled for the current time or the future are returned.</p>
     *
     * @param userId The id of the user whose events are to be fetched.
     * @return A {@link LiveData} list of event objects for the specified user.
     */
    @Query("SELECT * FROM events WHERE userId = :userId AND eventTime >= strftime('%s', 'now') * 1000 ORDER BY eventTime ASC")
    LiveData<List<Event>> getEventsForUser(long userId);

    /**
     * Fetches a specific event by id.
     *
     * @param eventId The id of the event to fetch.
     * @return The event with the specified id.
     * Returns {@code null} if no event is found.
     */
    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    Event getEvent(long eventId);

    /**
     * Deletes an event with the specified id.
     *
     * @param eventId The id of the event to delete.
     */
    @Query("DELETE FROM events WHERE id = :eventId")
    void deleteEvent(long eventId);
}
