package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddMedLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_med_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}