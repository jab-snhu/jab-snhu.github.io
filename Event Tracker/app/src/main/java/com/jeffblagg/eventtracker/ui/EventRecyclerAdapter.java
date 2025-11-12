/*
 * EventRecyclerAdapter.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.ui;

import com.jeffblagg.eventtracker.R;
import com.jeffblagg.eventtracker.entities.Event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for binding {@link Event} objects to card views in a RecyclerView.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventsHolder> {
    /**
     * Interface for a callback after the delete event button has been clicked.
     */
    public interface OnDeleteClicked { void onDelete(Event event); }
    /**
     * Interface for a callback after an event card has been clicked.
     */
    public interface OnEventClicked { void onClick(Event event); }

    private final List<Event> events = new ArrayList<>();
    private final OnDeleteClicked onDeleteClicked;
    private final OnEventClicked onEventClicked;

    // date and time formatters
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

    /**
     * EventRecyclerAdapter constructor created with required callbacks.
     *
     * @param onDeleteClicked Callback triggered when a card's delete button is clicked.
     * @param onEventClicked Callback triggered when a card is clicked.
     */
    public EventRecyclerAdapter(@NonNull OnDeleteClicked onDeleteClicked, @NonNull OnEventClicked onEventClicked) {
        this.onDeleteClicked = onDeleteClicked;
        this.onEventClicked = onEventClicked;
    }

    /**
     * Clears the current list of events and adds the updated ones.
     *
     * @param eventList The updated list of events to display.
     */
    public void submit(List<Event> eventList) {
        events.clear();
        if (eventList != null) {
            events.addAll(eventList);
        }
        notifyDataSetChanged();
    }

    /**
     * Inflates the event card layout and returns a new holder.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return The EventsHolder with the inflated event card.
     */
    @NonNull @Override
    public EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_card, parent, false);
        return new EventsHolder(view);
    }

    /**
     * Binds an event to its card for the list.
     *
     * @param eventsHolder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventsHolder eventsHolder, int position) {
        Event event = events.get(position);

        eventsHolder.titleTextView.setText(event.title);
        String eventDate = dateFormat.format(new java.util.Date(event.eventTime));
        String eventTime = timeFormat.format(new java.util.Date(event.eventTime));
        String dateTimeString = eventDate + " - " + eventTime;
        eventsHolder.dateTextView.setText(dateTimeString);

        eventsHolder.descriptionTextView.setText(event.description == null ? "" : event.description);

        if (event.cardColor != null) {
            eventsHolder.eventCard.setCardBackgroundColor(event.cardColor);
        }

        eventsHolder.itemView.setOnClickListener(v -> onEventClicked.onClick(event));
        eventsHolder.deleteButton.setOnClickListener(v -> onDeleteClicked.onDelete(event));
    }

    /**
     * Fetches the size of the events list.
     * @return The size of the event list as an {@code int}.
     */
    @Override public int getItemCount() {
        return events.size();
    }

    /**
     * ViewHolder class for the event cards.
     */
    public static class EventsHolder extends RecyclerView.ViewHolder {
        final CardView eventCard;
        final TextView
                titleTextView,
                dateTextView,
                descriptionTextView;
        final ImageButton deleteButton;

        /**
         * EventsHolder constructor. Finds the view references and assigns them
         * to the class properties.
         */
        EventsHolder(@NonNull View root) {
            super(root);
            eventCard = (CardView) root;
            titleTextView = root.findViewById(R.id.cardTitleTextView);
            dateTextView = root.findViewById(R.id.cardDateTextView);
            descriptionTextView = root.findViewById(R.id.descriptionTextView);
            deleteButton = root.findViewById(R.id.deleteButton);
        }
    }
}
