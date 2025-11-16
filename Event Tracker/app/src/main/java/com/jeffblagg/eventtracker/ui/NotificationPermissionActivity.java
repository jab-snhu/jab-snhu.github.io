/*
 * NotificationPermissionActivity.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jeffblagg.eventtracker.R;
import com.jeffblagg.eventtracker.UserSessionManager;
import com.jeffblagg.eventtracker.reminder.EventReminderManager;

/**
 * Activity to prepare users for the SMS permission request.
 */
public class NotificationPermissionActivity extends AppCompatActivity {
    // assign a value for the permission request
    private static final int PERMISSION_REQ = 67;
    private Button allowButton;
    private Button skipButton;

    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_permission);

        // initialize session manager and setup views
        sessionManager = new UserSessionManager(this);
        findViews();

        // add button listeners
        allowButton.setOnClickListener(v -> {
            requestSMSPermission();
        });

        skipButton.setOnClickListener(v -> {
            navigateToEventsActivity();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initializes the instance view instance variables by finding the views
     * in the layout by id.
     */
    private void findViews() {
        allowButton = findViewById(R.id.allowButton);
        skipButton = findViewById(R.id.skipButton);
    }

    /**
     * Navigates to the main Events activity.
     */
    private void navigateToEventsActivity() {
        Intent eventsIntent = new Intent(this, EventsActivity.class);
        eventsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(eventsIntent);
        finish();
    }

    /**
     * Requests SMS permission. If already granted, skips the request dialog and
     * navigates directly to the events activity.
     */
    private void requestSMSPermission() {
        if (sessionManager.smsPermissionGranted(this)) {
            Toast.makeText(this, "SMS permission granted.", Toast.LENGTH_SHORT).show();
            navigateToEventsActivity();
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                PERMISSION_REQ );
    }

    /**
     * Handles the result of the SMS permission request. The username is stored
     * as having made an SMS decisions so they do not get the screen or dialog multiple times.
     *
     * @param requestCode The request code passed in.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ) {
            String userId = sessionManager.getUserId();
            // record that the user has made an SMS decision
            sessionManager.setSMSDecisionMade(userId, true);
        }

        // navigate to events regardless of the decision
        navigateToEventsActivity();
    }
}