package com.example.d.studyjournal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by d on 2017-12-10.
 */

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            ReminderNotification.notify(context)
        } catch(e: Exception) {}
    }
}