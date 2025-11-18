/*
 * EventsActivity.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import com.jeffblagg.eventtracker.R;
import com.jeffblagg.eventtracker.viewmodel.EventsViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Main activity for the app, displays the logged in user's upcoming events.
 */
public class EventsActivity extends AppCompatActivity {
    private Toolbar eventsToolbar;
    private FloatingActionButton fab;
    private RecyclerView eventsRecyclerView;
    private TextView emptyStateTextView;

    private EventsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);

        // initialize view model and views
        viewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        findViews();
        setSupportActionBar(eventsToolbar);
        setupRecyclerView();
        loadEvents();

        // FAB navigates to Add/Edit Event activity
        fab.setOnClickListener(v -> {
            Intent addEditIntent = new Intent(EventsActivity.this, AddEditEventActivity.class);
            startActivity(addEditIntent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Inflates the overflow menu with the sign out option.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return true;
    }

    /**
     * Handles the menu selections
     * @param item The selected menu item.
     * @return Returns {@code true} after handling.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            handleSignOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logs the user out and returns to the LoginActivity as the root activity.
     */
    private void handleSignOut() {
        viewModel.signOut();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    /**
     * Initializes the instance view instance variables by finding the views
     * in the layout by id.
     */
    private void findViews() {
        eventsToolbar = findViewById(R.id.eventsToolbar);
        fab = findViewById(R.id.addEventFAB);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
    }

    /**
     * Sets up the RecyclerView with a single-column Grid layout.
     */
    private void setupRecyclerView() {
        eventsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        EventRecyclerAdapter adapter = new EventRecyclerAdapter(
                event ->
                        viewModel.deleteEvent(event.id,
                        () -> Toast.makeText(this, "Event deleted.", Toast.LENGTH_SHORT).show()),
                event -> {
                    Intent intent = new Intent(this, AddEditEventActivity.class);
                    intent.putExtra(AddEditEventActivity.EDIT_EVENT_ID, event.id);
                    startActivity(intent);
                });

        eventsRecyclerView.setAdapter(adapter);
    }

    /**
     * Loads and observes the user's future events.
     */
    private void loadEvents() {
        String userId = viewModel.getCurrentUserId();

        if (userId == null) {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EventRecyclerAdapter adapter = (EventRecyclerAdapter) eventsRecyclerView.getAdapter();
        if (adapter != null) {
            viewModel.userEvents(userId).observe(this, events -> {
                adapter.submit(events);
                emptyStateTextView.setVisibility(
                        (events == null || events.isEmpty())
                                ? View.VISIBLE
                                : View.GONE);
            });
        }
    }
}