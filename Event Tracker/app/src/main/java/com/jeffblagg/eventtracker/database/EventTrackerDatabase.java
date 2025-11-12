/*
 * EventTrackerDatabase.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.database;

import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.entities.User;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The abstract Room database class for the app, defining the SQLite database
 * configuration.
 *
 * <p>Entities:</p>
 * <ul>
 *     <li>{@link User} - The created user accounts for the app.</li>
 *     <li>{@link Event} - The created user events.</li>
 * </ul>
 *
 * <p>Data Access Objects (DAOs):</p>
 * <ul>
 *     <li>{@link UserDao} - CRUD operation handler for users.</li>
 *     <li>{@link EventDao} - CRUD operation handler for events.</li>
 * </ul>
 */
@Database(entities = {User.class, Event.class}, version = 1, exportSchema = false)
public abstract class EventTrackerDatabase extends RoomDatabase {
   /**
    * Provides access to user database operations.
    *
    * @return The DAO for user operations.
    */
   public abstract UserDao userDao();

   /**
    * Provides access to event database operations.
    * @return The DAO for event operations.
    */
   public abstract EventDao eventDao();
}
