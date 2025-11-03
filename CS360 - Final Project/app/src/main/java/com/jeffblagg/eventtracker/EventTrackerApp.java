/*
 * EventTrackerApp.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker;

import com.jeffblagg.eventtracker.database.EventTrackerDatabase;

import android.app.Application;

import static androidx.room.Room.databaseBuilder;

/**
 * Custom {@link Application} class for initializing the Event
 * Tracker database.
 *
 * <p>The database is created using Room on app start.</p>
 */
public class EventTrackerApp extends Application {

    /**
     * Singleton instance of the Room database.
     */
    private EventTrackerDatabase db;

    /**
     * Initializes the Room database on app start.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        db = databaseBuilder(
                this,
                EventTrackerDatabase.class,
                "event_tracker.db")
                .build();
    }

    /**
     * Getter for the database instance.
     *
     * @return The database for the app.
     */
    public EventTrackerDatabase getDatabase() {
        return db;
    }
}
