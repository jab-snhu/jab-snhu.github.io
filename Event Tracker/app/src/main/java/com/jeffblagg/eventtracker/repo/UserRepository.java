/*
 * UserRepository.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.repo;

import com.jeffblagg.eventtracker.EventTrackerApp;
import com.jeffblagg.eventtracker.database.EventTrackerDatabase;
import com.jeffblagg.eventtracker.entities.User;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link User} Repository class that facilitates User DAO operations
 * for the rest of the app.
 */
public class UserRepository {
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
     * Interface for a callback after an user has been loaded.
     */
    public interface OnUserLoaded { void onResult(User user); }

    /**
     * Interface for a callback after an user has been added.
     */
    public interface OnUserAdded { void onResult(long userId); }

    /**
     * UserRepository constructor.
     *
     * @param application The application object used to access the database.
     */
    public UserRepository(Application application) {
        db = ((EventTrackerApp) application).getDatabase();
    }

    /**
     * Fetches a user with the provided username.
     *
     * @param username The username of the user to fetch.
     * @param callback Action receiving the fetched user.
     */
    public void getUser(String username, OnUserLoaded callback) {
        databaseExecutor.execute(() -> {
            User user = db.userDao().getUser(username);
            mainLooper.post(() -> callback.onResult(user));
        });
    }

    /**
     * Adds a new user.
     *
     * @param user The user to add.
     * @param callback Action receiving the added user's id.
     */
    public void addUser(User user, OnUserAdded callback) {
        databaseExecutor.execute(() -> {
            long id = db.userDao().insert(user);
            mainLooper.post(() -> callback.onResult(id));
        });
    }
}
