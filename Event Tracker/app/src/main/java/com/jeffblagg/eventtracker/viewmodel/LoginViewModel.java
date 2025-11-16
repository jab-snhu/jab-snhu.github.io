/*
 * EventsViewModel.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.viewmodel;

import com.jeffblagg.eventtracker.authentication.AuthManager;
import com.jeffblagg.eventtracker.authentication.FirebaseAuthManager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * The view model for the LoginActivity. Contains an authentication
 * manager to handle auth operations.
 */
public class LoginViewModel extends AndroidViewModel {
    private final AuthManager authManager;

    /**
     * LoginViewModel constructor. Initializes the user repository.
     *
     * @param application The application context.
     */
    public LoginViewModel(@NonNull Application application) {
        super(application);
        authManager = new FirebaseAuthManager();
    }

    /**
     * Interface for a callback after logging in with methods for
     * successful login, user not found, and encountered errors.
     */
    public interface LoginCallback {
        void onSuccess(String userId);
        void onUserNotFound();
        void onError(String errorMessage);
    }

    /**
     * Interface for a callback when creating a new user with methods
     * for successful user creation and an encountered error.
     */
    public interface CreateUserCallback {
        void onSuccess(String userId);
        void onError(String errorMessage);
    }

    /**
     * Attempts to log the user in with the provided email and password.
     *
     * @param email The email for the user.
     * @param password The password for the user.
     * @param callback The callback triggered with the result from fetching the user.
     */
    public void login(@NonNull String email, @NonNull String password, LoginCallback callback) {
        authManager.signIn(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(AuthManager.AuthError error) {
                if (error == AuthManager.AuthError.USER_NOT_FOUND) {
                    callback.onUserNotFound();
                } else {
                    callback.onError(error.message);
                }
            }
        });
    }

    /**
     * Creates a new user using the provided email and password.
     *
     * @param email The email for the user.
     * @param password The password for the user.
     * @param callback The callback triggered after attempting to create a new user.
     */
    public void createUser(@NonNull String email,
                           @NonNull String password,
                           CreateUserCallback callback) {
        authManager.createAccount(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(String userId) {
                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(AuthManager.AuthError error) {
                callback.onError(error.message);
            }
        });
    }
}
