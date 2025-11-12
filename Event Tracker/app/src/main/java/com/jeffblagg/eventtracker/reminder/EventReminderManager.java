/*
 * EventReminderManager.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.reminder;

import com.jeffblagg.eventtracker.UserSessionManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Manages scheduling of SMS reminders for events.
 *
 * <p>Reminders are only scheduled for future events and if the
 * {@link android.Manifest.permission#SEND_SMS} permission has been granted.</p>
 */
public final class EventReminderManager {
   /**
    * Creates a {@link PendingIntent} used for an SMS reminder.
    *
    * @param context The application context.
    * @param eventId The id of the scheduled event.
    * @param message The message for the scheduled SMS.
    * @param flags Flags for the PendingIntent
    * @return The configured PendingIntent for the scheduled event.
    */
   private static PendingIntent buildPendingIntent(Context context, long eventId, String message, int flags) {
      Intent intent = new Intent(context, EventReminderReceiver.class);
      // hard code the phone number to "5554" to only send to the emulator
      intent.putExtra(EventReminderReceiver.PHONE_NUMBER, "5554");
      intent.putExtra(EventReminderReceiver.SMS_MESSAGE, message);
      return PendingIntent.getBroadcast(context, (int) eventId, intent, flags);
   }

   /**
    * Schedules a new SMS reminder for an event at the specified time. Events are only
    * scheduled if the reminder time is in the future and the user has granted SMS permissions
    * through {@link android.Manifest.permission#SEND_SMS}.
    *
    * @param context The context for the scheduled message.
    * @param eventId The id of the event.
    * @param reminderTime The time for the reminder to be sent, in milliseconds.
    * @param message The message used as the SMS body
    */
   public static void schedule(Context context, long eventId, long reminderTime, String message) {
      UserSessionManager sessionManager = new UserSessionManager(context);

      // block scheduling if reminder time is in the past or permission hasn't been granted
      if (reminderTime <= System.currentTimeMillis() || !sessionManager.smsPermissionGranted(context)) {
         return;
      }

      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent = buildPendingIntent(
              context,
              eventId,
              message,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      if (alarmManager != null && pendingIntent != null) {
         alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
      }
   }

   /**
    * Cancels a previously scheduled reminder for a specified event.
    *
    * @param context The context for scheduled events.
    * @param eventId The id of the event used to lookup any existing reminders.
    */
   public static void cancel(Context context, long eventId) {
      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent = buildPendingIntent(
              context,
              eventId,
              null,
              PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);

      if (alarmManager != null && pendingIntent != null) {
         alarmManager.cancel(pendingIntent);
      }
   }
}
