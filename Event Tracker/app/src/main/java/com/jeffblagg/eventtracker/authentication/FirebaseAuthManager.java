/*
 * FirebaseAuthManager.java
 *
 * Author: Jeff Blagg
 * Class: CS-499 - CS Capstone
 * Date: November 2025
 */

package com.jeffblagg.eventtracker.authentication;

import com.google.firebase.auth.*;

/**
 * A manager class to handle all authentication actions with Firebase
 * Authentication. Implements the AuthManager interface.
 */
public class FirebaseAuthManager implements AuthManager {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            return user.getUid();
        }

        return null;
    }

    @Override
    public void signIn(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            callback.onSuccess(user.getUid());
                        } else {
                            // This should never happen, but in case sign in is
                            // successful but the user is null, send an error
                            callback.onFailure(AuthError.OTHER);
                        }
                    } else {
                        callback.onFailure(authErrorFromException(task.getException()));
                    }
                });
    }

    @Override
    public void createAccount(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            callback.onSuccess(user.getUid());
                        } else {
                            // This should never happen, but in case account creation
                            // is successful but the user is null, send an error
                            callback.onFailure(AuthError.OTHER);
                        }
                    } else {
                        callback.onFailure(authErrorFromException(task.getException()));
                    }
                });
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    /**
     * Provides an appropriate AuthError for a provided Firebase exception.
     *
     * @param exception the Firebase exception
     * @return the mapped AuthError
     */
    private AuthError authErrorFromException(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            return AuthError.USER_NOT_FOUND;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return AuthError.INVALID_PASSWORD;
        } else {
            return AuthError.OTHER;
        }
    }
}
