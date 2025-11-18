/*
 * LoginActivity.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import com.jeffblagg.eventtracker.R;
import com.jeffblagg.eventtracker.viewmodel.LoginViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

/**
 * Activity to create a new account or login with an existing one.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // initialize view model, and views
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        findViews();
        setupTextListeners();
        setupLoginButton();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Handles the login process. Tries first to find the user in the database. If
     * authentication fails, creates a new account.
     */
    private void handleLoginButtonClick() {
        String email = emailEditText.getText().toString().toLowerCase();
        String password = passwordEditText.getText().toString();

        viewModel.login(email, password, new LoginViewModel.LoginCallback() {
            @Override
            public void onSuccess(String userId) {
                handleSuccessfulLogin();
            }

            @Override
            public void onUserNotFound() {
                createUser(email, password);
            }

            @Override
            public void onError(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    /**
     * Routes a newly logged in user to the appropriate screen.
     * If the user has already granted SMS permissions or has already
     * made a decision regarding SMS permissions, they are routed directly
     * to the {@link EventsActivity}. Otherwise, they are routed to the
     * {@link NotificationPermissionActivity}.
     */
    private void handleSuccessfulLogin() {
        boolean smsPermissionGranted = viewModel.smsPermissionGranted();
        boolean userHasDecidedSMS = viewModel.userHasDecidedSMS();
        Intent nextIntent;

        // if the user has already made a decision surrounding notifications,
        // take them straight to the Events activity
        if (smsPermissionGranted || userHasDecidedSMS) {
            nextIntent = new Intent(this, EventsActivity.class);
        } else {
            nextIntent = new Intent(this, NotificationPermissionActivity.class);
        }

        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextIntent);
        finish();
    }

    /**
     * Creates a new user, then proceeds with the login flow.
     *
     * @param email The email for the new user.
     * @param password The password for the new user.
     */
    private void createUser(String email, String password) {
        viewModel.createUser(email, password, new LoginViewModel.CreateUserCallback() {
            @Override
            public void onSuccess(String userId) {
                handleSuccessfulLogin();
            }

            @Override
            public void onError(String errorMessage) {
                showError(errorMessage);
            }
        });
    };

    /**
     * Initializes the instance view instance variables by finding the views
     * in the layout by id.
     */
    private void findViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.allowButton);
    }

    /**
     * Adds text listeners to enable or disable the login button.
     */
    private void setupTextListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // Intentionally left blank
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                loginButton.setEnabled(!email.isBlank() && !password.isBlank());
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
    }

    /**
     * Initializes the login button and adds the text listener.
     */
    private void setupLoginButton() {
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(v -> handleLoginButtonClick());
    }

    /**
     * Displays a toast when an error is encountered.
     * @param errorMessage The errorMessage to display.
     */
    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}