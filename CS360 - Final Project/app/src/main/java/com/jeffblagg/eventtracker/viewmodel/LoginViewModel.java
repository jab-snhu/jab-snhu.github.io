/*
 * EventsViewModel.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.viewmodel;

import com.jeffblagg.eventtracker.entities.User;
import com.jeffblagg.eventtracker.repo.UserRepository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * The view model for the LoginActivity. Connects to the
 * {@link UserRepository} to verify an existing user or create a
 * new one.
 */
public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepo;

    /**
     * LoginViewModel constructor. Initializes the user repository.
     *
     * @param application The application context.
     */
    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
    }

    /**
     * Interface for a callback after logging in with methods for
     * successful login, user not found, and encountered errors.
     */
    public interface LoginCallback {
        void onSuccess(long userId);
        void onUserNotFound();
        void onError(String errorMessage);
    }

    /**
     * Interface for a callback when creating a new user with methods
     * for successful user creation and an encountered error.
     */
    public interface CreateUserCallback {
        void onSuccess(long userId);
        void onError(String errorMessage);
    }

    /**
     * Attempts to find a matching user in the database, matching against
     * username and password.
     *
     * @param username The username for the user.
     * @param password The password for the user.
     * @param callback The callback triggered with the result from fetching the user.
     */
    public void login(@NonNull String username, @NonNull String password, LoginCallback callback) {
        // convert username to lowercase before fetching
        final String lowercaseUsername = username.toLowerCase();

        userRepo.getUser(lowercaseUsername, user -> {
            if (user == null) {
                callback.onUserNotFound();
            } else if (password.equals(user.password)) {
                callback.onSuccess(user.id);
            } else {
                callback.onError("Invalid password.");
            }
        });
    }

    /**
     * Creates a new user using the provided username and password. Usernames
     * are converted to lowercase to avoid case sensitivity.
     *
     * @param username The username for the user.
     * @param password The password for the user.
     * @param callback The callback triggered after attempting to create a new user.
     */
    public void createUser(@NonNull String username,
                           @NonNull String password,
                           CreateUserCallback callback) {
        // use a lowercase username to avoid case sensitivity
        final String lowercaseUsername = username.toLowerCase();

        User newUser = new User(lowercaseUsername, password);

        userRepo.addUser(newUser, newUserId -> {
            if (newUserId > 0) {
                callback.onSuccess(newUserId);
            } else {
                callback.onError("Failed to create user.");
            }
        });
    }
}
