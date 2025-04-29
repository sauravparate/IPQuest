package com.example.ipquest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val addIPCard = findViewById<CardView>(R.id.addIPCard)
        val ipListCard = findViewById<CardView>(R.id.ipListCard)
        val manageIPCard = findViewById<CardView>(R.id.manageIPCard)
        val deadlineCard = findViewById<CardView>(R.id.deadlineCard)

        addIPCard.setOnClickListener {
            startActivity(Intent(this, AddIPActivity::class.java))
        }

        ipListCard.setOnClickListener {
            startActivity(Intent(this, IPList::class.java))
        }

        manageIPCard.setOnClickListener {
            startActivity(Intent(this, ManageIP::class.java))
        }

        deadlineCard.setOnClickListener {
            startActivity(Intent(this, UpcomingDeadlines::class.java))
        }
    }
}
