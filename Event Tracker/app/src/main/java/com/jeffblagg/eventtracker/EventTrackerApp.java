/*
 * EventTrackerApp.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker;

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
     * Initializes the Room database on app start.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
