package com.example.ipquest

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddIPActivity : AppCompatActivity() {

    private lateinit var ipNameEditText: EditText
    private lateinit var ipTypeSpinner: Spinner
    private lateinit var ipDescriptionEditText: EditText
    private lateinit var deadlineButton: Button
    private lateinit var saveButton: Button
    private lateinit var selectedDate: String
    private lateinit var calendar: Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ip)

        ipNameEditText = findViewById(R.id.ipNameEditText)
        ipTypeSpinner = findViewById(R.id.ipTypeSpinner)
        ipDescriptionEditText = findViewById(R.id.ipDescriptionEditText)
        deadlineButton = findViewById(R.id.deadlineButton)
        saveButton = findViewById(R.id.saveButton)

        // Setup spinner with IP types
        val ipTypes = arrayOf("Patent", "Trademark", "Copyright", "Design", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ipTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ipTypeSpinner.adapter = adapter

        // Set up calendar
        calendar = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = "${dayOfMonth}/${month + 1}/${year}"
            selectedDate = date
            deadlineButton.text = "Deadline: $date"
        }

        // Open date picker when deadline button is clicked
        deadlineButton.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Save IP and set alarm for the deadline
        saveButton.setOnClickListener {
            val ipName = ipNameEditText.text.toString()
            val ipType = ipTypeSpinner.selectedItem.toString()
            val ipDescription = ipDescriptionEditText.text.toString()

            if (ipName.isNotEmpty() && ipDescription.isNotEmpty() && ::selectedDate.isInitialized) {
                // Save data logic (can use SharedPreferences, Database, or any other storage)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // Calculate deadline in milliseconds
                val deadlineInMillis = calendar.timeInMillis

                // Check if we need permission for exact alarm
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!hasExactAlarmPermission()) {
                        // Request permission for scheduling exact alarms
                        Toast.makeText(this, "Please enable the 'Schedule Exact Alarms' permission in settings", Toast.LENGTH_LONG).show()
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        startActivity(intent)
                    } else {
                        // Set the alarm if permission is granted
                        setAlarmForDeadline(deadlineInMillis)
                    }
                } else {
                    // For versions below Android 12, you can directly set the alarm
                    setAlarmForDeadline(deadlineInMillis)
                }
            } else {
                Toast.makeText(this, "Please fill all fields and set a deadline", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Check if the app has permission to schedule exact alarms
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true // Permission is always granted for API levels lower than Android 12
        }
    }

    private fun setAlarmForDeadline(deadlineInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra("IP_NAME", ipNameEditText.text.toString())
        alarmIntent.putExtra("IP_DEADLINE", selectedDate)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadlineInMillis, pendingIntent)
        Toast.makeText(this, "Alarm set for deadline", Toast.LENGTH_SHORT).show()
    }
}
