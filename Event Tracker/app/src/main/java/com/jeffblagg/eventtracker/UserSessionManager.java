/*
 * UserSessionManager.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages user session and SMS notification preferences for the app
 * Data is stored locally in {@link SharedPreferences}.
 */
public class UserSessionManager {
    /** {@link SharedPreferences} file name for the app's stored data. */
    private static final String PREF_NAME = "event_tracker_prefs";

    /** Storage key for the current user's id */
    private static final String CURRENT_USER_ID_KEY = "current_user_id";

    /** Storage key for record of whether a user has made a decision about SMS permission */
    private static final String SMS_DECISIONS_MADE_KEY = "sms_decisions_made";

    /** {@link SharedPreferences} instance for local storage persistence needs */
    private final SharedPreferences prefs;

    /**
     * UserSessionManager constructor
     *
     * @param context Context used to access shared preferences.
     */
    public UserSessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves the id of the current user.
     *
     * @param userId the id of the current user.
     */
    public void setLoggedInUser(long userId) {
        prefs.edit()
                .putLong(CURRENT_USER_ID_KEY, userId)
                .apply();
    }

    /**
     * Fetches the id of the current user.
     *
     * @return The id ({@code long}) of the currently logged in user.
     */
    public long getUserId() {
        return prefs.getLong(CURRENT_USER_ID_KEY, -1L);
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return {@code true} if a user ID is stored, {@code false} otherwise.
     */
    public boolean isLoggedIn() {
        return getUserId() != -1L;
    }

    /**
     * Logs the current user out by removing the stored id in shared preferences.
     */
    public void logoutUser() {
        prefs.edit().remove(CURRENT_USER_ID_KEY).apply();
    }

    /**
     * Saves whether a user has made a decision regarding SMS permissions.
     * <p>NOTE: This method does not store what the user's decision was, just
     * whether they have made one.</p>
     *
     * @param userId The id of the user whose decision is being saved.
     * @param decision {@code true} if the user has made a decision regard SMS
     *                             permission, {@code false} otherwise (though
     *                             that should not be recorded).
     */
    public void setSMSDecisionMade(long userId, boolean decision) {
        try {
            // get the existing set of saved user decisions
            String jsonString = prefs.getString(SMS_DECISIONS_MADE_KEY, "{}");
            JSONObject jsonObject = new JSONObject(jsonString);

            // add the decision for the current user and save
            jsonObject.put(String.valueOf(userId), decision);
            prefs.edit().putString(SMS_DECISIONS_MADE_KEY, jsonObject.toString()).apply();
        } catch (JSONException exception) {
            Log.d("UserSessionManager", "JSON Exception " + exception);
        }
    }

    /**
     * Checks if a user has made a decision regarding SMS permissions.
     *
     * @param userId The user id to check.
     * @return {@code true} if a decision exists for the user, {@code false} otherwise.
     */
    public boolean userHasDecidedSMS(long userId) {
        try {
            String jsonString = prefs.getString(SMS_DECISIONS_MADE_KEY, "{}");
            JSONObject jsonObject = new JSONObject(jsonString);

            // return the stored value, or false if no entry exists for the user
            return jsonObject.optBoolean(String.valueOf(userId), false);
        } catch (JSONException exception) {
            Log.d("UserSessionManager", "JSON Exception " + exception);
            return false;
        }
    }

    /**
     * Checks the system SMS permission status.
     *
     * @param context The context to check permissions with.
     * @return {@code true} if {@link Manifest.permission#SEND_SMS} is
     * granted, {@code false} otherwise
     */
    public boolean smsPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
}
