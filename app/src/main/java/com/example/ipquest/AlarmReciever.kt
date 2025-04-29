package com.example.ipquest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val ipName = intent.getStringExtra("IP_NAME")
        val ipDeadline = intent.getStringExtra("IP_DEADLINE")

        // You can show a notification or a toast message when the alarm triggers
        Toast.makeText(context, "Reminder: IP '$ipName' deadline is $ipDeadline", Toast.LENGTH_LONG).show()
    }
}
