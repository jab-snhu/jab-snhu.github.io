/*
 * SMSPermissionManager.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.reminder;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages SMS notification preferences for the app.
 * Data is stored locally in {@link SharedPreferences}.
 */
public class SMSPermissionManager {
    /** {@link SharedPreferences} file name for the app's stored data. */
    private static final String PREF_NAME = "event_tracker_prefs";

    /** Storage key for record of whether a user has made a decision about SMS permission */
    private static final String SMS_DECISIONS_MADE_KEY = "sms_decisions_made";

    /** {@link SharedPreferences} instance for local storage persistence needs */
    private final SharedPreferences prefs;

    /**
     * SMSPermissionManager constructor
     *
     * @param context Context used to access shared preferences.
     */
    public SMSPermissionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
    public void setSMSDecisionMade(String userId, boolean decision) {
        try {
            // get the existing set of saved user decisions
            String jsonString = prefs.getString(SMS_DECISIONS_MADE_KEY, "{}");
            JSONObject jsonObject = new JSONObject(jsonString);

            // add the decision for the current user and save
            jsonObject.put(userId, decision);
            prefs.edit().putString(SMS_DECISIONS_MADE_KEY, jsonObject.toString()).apply();
        } catch (JSONException exception) {
            Log.d("SMSPermissionManager", "JSON Exception " + exception);
        }
    }

    /**
     * Checks if a user has made a decision regarding SMS permissions.
     *
     * @param userId The user id to check.
     * @return {@code true} if a decision exists for the user, {@code false} otherwise.
     */
    public boolean userHasDecidedSMS(String userId) {
        try {
            String jsonString = prefs.getString(SMS_DECISIONS_MADE_KEY, "{}");
            JSONObject jsonObject = new JSONObject(jsonString);

            // return the stored value, or false if no entry exists for the user
            return jsonObject.optBoolean(userId, false);
        } catch (JSONException exception) {
            Log.d("SMSPermissionManager", "JSON Exception " + exception);
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
