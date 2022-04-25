package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddBathroomLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bathroom_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_bathroom_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}