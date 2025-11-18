/*
 * NotificationPermissionViewModel.java
 *
 * Author: Jeff Blagg
 * Class: CS-499 - CS Capstone
 * Date: November 2025
 */

package com.jeffblagg.eventtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.jeffblagg.eventtracker.authentication.AuthManager;
import com.jeffblagg.eventtracker.authentication.FirebaseAuthManager;
import com.jeffblagg.eventtracker.reminder.SMSPermissionManager;

/**
 * View model for the NotificationPermissionActivity.
 * Handles SMS permissions for the user.
 */
public class NotificationPermissionViewModel extends AndroidViewModel {
    private final SMSPermissionManager smsPermissionManager;
    private final AuthManager authManager;

    /**
     * NotificationPermissionViewModel constructor.
     * @param application The application context.
     */
    public NotificationPermissionViewModel(@NonNull Application application) {
        super(application);
        smsPermissionManager = new SMSPermissionManager(application);
        authManager = new FirebaseAuthManager();
    }

    /**
     * Checks if SMS permission ha been granted.
     *
     * @return true if SMS permission has been granted, false if not.
     */
    public boolean smsPermissionGranted() {
        return smsPermissionManager.smsPermissionGranted(getApplication());
    }

    /**
     * Records that the current user has made a decision about SMS.
     */
    public void recordSMSDecision() {
        String userId = authManager.getCurrentUserId();
        if (userId != null) {
            smsPermissionManager.setSMSDecisionMade(userId, true);
        }
    }
}
