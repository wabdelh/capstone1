package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddSleepLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sleep_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_sleep_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}