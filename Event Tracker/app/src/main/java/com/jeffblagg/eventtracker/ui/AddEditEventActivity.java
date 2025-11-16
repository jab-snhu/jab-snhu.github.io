/*
 * AddEditEventActivity.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.jeffblagg.eventtracker.R;
import com.jeffblagg.eventtracker.UserSessionManager;
import com.jeffblagg.eventtracker.entities.Event;
import com.jeffblagg.eventtracker.viewmodel.AddEditEventViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activity for creating a new {@link Event} or editing an existing one.
 */
public class AddEditEventActivity extends AppCompatActivity {
    /**
     * Intent extra key for the id of the event to edit (if provided).
     */
    public static final String EDIT_EVENT_ID = "EVENT_ID";

    /**
     * Palette of possible card colors.
     */
    private static final int[] COLOR_PALETTE = new int[]{
            0xFF475D92,
            0xFF7B1FA2,
            0xFF2E7D32,
            0xFFEF6C00,
            0xFF00695C
    };

    private AddEditEventViewModel viewModel;

    // use default value of -1L for a new event
    private long eventId = -1L;

    private Toolbar toolbar;
    private ConstraintLayout eventCardLayout;
    private EditText
            nameEditText,
            dateEditText,
            timeEditText,
            descriptionEditText;

    private ImageButton colorsButton;
    private Button addEditButton;
    private Button cancelButton;

    private final Calendar calendar = Calendar.getInstance();

    // set a default card background color
    private Integer selectedColor = 0xFF475D92;
    private int colorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_event);

        viewModel = new ViewModelProvider(this).get(AddEditEventViewModel.class);
        eventId = getIntent().getLongExtra(EDIT_EVENT_ID, -1L);

        // activity initializations
        findViews();
        setupToolbar();
        setupTextListeners();

        // add button and date/time listeners
        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());

        colorsButton.setOnClickListener(v -> cycleColor());
        addEditButton.setEnabled(false);
        addEditButton.setOnClickListener(v -> saveEvent());
        cancelButton.setOnClickListener(v -> cancel());

        // if an event id is provided load it, otherwise update UI
        // to add a new event
        if (eventId > 0) {
            viewModel.loadEvent(eventId, this::populateEventInfo);
            addEditButton.setText(R.string.edit_event);
        } else {
            updateDateTimeFields();
            addEditButton.setText(R.string.add_event);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Finds views in the layout and adds them to the local properties
     */
    private void findViews() {
        toolbar = findViewById(R.id.addEditEventToolbar);
        setSupportActionBar(toolbar);

        eventCardLayout = findViewById(R.id.eventCardLayout);
        nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        colorsButton = findViewById(R.id.colorsButton);
        addEditButton = findViewById(R.id.addEditButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    /**
     * Adds a text watcher to verify an event name exists before
     * enabling the add/edit button.
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
                String eventName = nameEditText.getText().toString();
                addEditButton.setEnabled(!eventName.isBlank());
            }
        };

        nameEditText.addTextChangedListener(textWatcher);
    }

    /**
     * Setup the toolbar with the correct title for adding or editing an event.
     */
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            String title = eventId > 0 ? "Edit Event" : "Add Event";
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Populates the UI with info from an existing event.
     *
     * @param event The event to display.
     */
    private void populateEventInfo(Event event) {
        if (event == null) {
            Toast.makeText(this, "Event not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        nameEditText.setText(event.title);
        descriptionEditText.setText(event.description != null ? event.description : "");

        calendar.setTimeInMillis(event.eventTime);
        updateDateTimeFields();

        selectedColor = event.cardColor;
        applyColorPreview();
    }

    /**
     * Updates the date and time fields with properly formatted text.
     */
    private void updateDateTimeFields() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        dateEditText.setText(dateFormat.format(calendar.getTime()));
        timeEditText.setText(timeFormat.format(calendar.getTime()));
    }

    /**
     * Shows a date picker and saves the chosen date for the event.
     */
    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, chosenYear, chosenMonth, chosenDay) -> {
            calendar.set(Calendar.YEAR, chosenYear);
            calendar.set(Calendar.MONTH, chosenMonth);
            calendar.set(Calendar.DAY_OF_MONTH, chosenDay);
            updateDateTimeFields();
        }, year, month, day).show();
    }

    /**
     * Shows a time picker and saves the chosen time for the event.
     */
    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, (view, chosenHour, chosenMinute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
            calendar.set(Calendar.MINUTE, chosenMinute);
            calendar.set(Calendar.SECOND, 0);
            updateDateTimeFields();
        }, hour, minute, false).show();
    }

    /**
     * Applies the selected color to the event card preview background.
     */
    private void applyColorPreview() {
        Drawable bg = eventCardLayout.getBackground();
        if (bg instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) bg.mutate();
            drawable.setColor(selectedColor);
        }
    }

    /**
     * Cycles through the defined color palette and updates the card background.
     */
    private void cycleColor() {
        colorIndex++;
        selectedColor = COLOR_PALETTE[colorIndex % COLOR_PALETTE.length];
        applyColorPreview();
    }

    /**
     * Validates input and either creates a new event or updates an existing one (if provided).
     */
    private void saveEvent() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        String userId = sessionManager.getUserId();

        // make sure there is a logged in user
        if (userId == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        long eventTime = calendar.getTimeInMillis();

        // require time to be in the future
        if (eventTime < System.currentTimeMillis()) {
            Toast.makeText(this, "Event must be in the future.", Toast.LENGTH_SHORT).show();
            return;
        }

        // require an event name
        if (title.isBlank()) {
            Toast.makeText(this, "Title is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // if an eventId exists, edit the existing event, otherwise add a new event
        if (eventId > 0) {
            viewModel.loadEvent(eventId, event -> {
                if (event == null) {
                    Toast.makeText(this, "Event not found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                event.title = title;
                event.description = description;
                event.eventTime = eventTime;
                event.cardColor = selectedColor;

                viewModel.updateEvent(event, rows -> {
                    Toast.makeText(this, "Event updated.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            });
        } else {
            viewModel.createNewEvent(userId, title, description, eventTime, selectedColor, id -> {
                if (id > 0) {
                    Toast.makeText(this, "Event added.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Add event failed.", Toast.LENGTH_SHORT).show();
                }
            }, errorMessage -> Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Cancels adding or editing the event and returns to the previous screen.
     */
    private void cancel() {
        finish();
    }
}