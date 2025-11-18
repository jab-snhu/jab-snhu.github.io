/*
 * LaunchActivity.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import com.jeffblagg.eventtracker.authentication.AuthManager;
import com.jeffblagg.eventtracker.authentication.FirebaseAuthManager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity is the app's entry point and is used to determine which screen
 * to display when the app starts. If a user is logged in, it routes to the EventsActivity.
 * Otherwise, route to the LoginActivity.
 */
public class LaunchActivity extends AppCompatActivity {
   @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // check login status and determine correct activity
      AuthManager authManager = new FirebaseAuthManager();
      Intent firstActivity = (authManager.getCurrentUserId() != null)
              ? new Intent(this, EventsActivity.class)
              : new Intent(this, LoginActivity.class);

      firstActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(firstActivity);
      finish();
   }
}
