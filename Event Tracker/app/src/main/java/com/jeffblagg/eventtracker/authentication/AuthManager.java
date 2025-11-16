/*
 * AuthManager.java
 *
 * Author: Jeff Blagg
 * Class: CS-499 - CS Capstone
 * Date: November 2025
 */

package com.jeffblagg.eventtracker.authentication;

/**
 * Interface for handling authentication processes. Provides methods for
 * creating accounts, signing in and out, and getting the id of the currently
 * logged in user.
 */
public interface AuthManager {

    /**
     * Represents the possible authentication errors relevant to the sign in
     * or account creation process.
     * <p>
     *     Possible values:
     * <ul>
     *     <li>{@link #USER_NOT_FOUND} - No account exists for the email provided</li>
     *     <li>{@link #INVALID_PASSWORD} - Invalid password provided</li>
     *     <li>{@link #OTHER} - Any other authentication error</li>
     * </ul>
     * </p>
     */
    public enum AuthError {
        USER_NOT_FOUND("No account exists with this email"),
        INVALID_PASSWORD("Invalid password"),
        OTHER("Authentication error occurred");

        public final String message;

        AuthError(String message) {
            this.message = message;
        }
    }

    /**
     * Interface for authentication callbacks
     */
    public interface AuthCallback {
        void onSuccess(String userId);
        void onFailure(AuthError error);
    }

    /**
     * Get the user ID for the currently logged in user.
     *
     * @return user ID or null if no user is logged in
     */
    String getCurrentUserId();

    /**
     * Attempts to sign a user in with the provided email and password.
     *
     * @param email the email address for the user
     * @param password the password for the user
     * @param callback the callback used to return the logged in user id if
     *                 successful or the appropriate {@link AuthError} if unsuccessful
     */
    void signIn(String email, String password, AuthCallback callback);

    /**
     * Creates a new account with the provided email and password.
     *
     * @param email the email for the new account
     * @param password the password for the new account
     * @param callback the callback used to return the user id of the created
     *                 account or the appropriate {@link AuthError} if unsuccessful
     */
    void createAccount(String email, String password, AuthCallback callback);

    /**
     * Signs the currently logged in user out.
     */
    void signOut();
}
