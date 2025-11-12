/*
 * EventReminderReceiver.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Receiver that handles sending SMS event reminders at a scheduled time.
 */
public class EventReminderReceiver extends BroadcastReceiver {
    /**
     * Intent extra key for the reminder phone number.
     */
    public static final String PHONE_NUMBER = "phone_number";

    /**
     * Intent extra key for the reminder message body.
     */
    public static final String SMS_MESSAGE = "sms_message";

    /**
     * Called when the scheduled broadcast intent is received at the scheduled time.
     *
     * @param context The context for the receiver.
     * @param intent The intent containing the reminder phone number and message.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra(PHONE_NUMBER);
        String message = intent.getStringExtra(SMS_MESSAGE);

        if (phoneNumber != null && message != null) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            } catch (Exception exception) {
                Log.d("EventReminderReceiver", "Failed to send SMS" ,exception);
            }
        }
    }
}
