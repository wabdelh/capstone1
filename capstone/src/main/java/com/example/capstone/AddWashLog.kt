package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddWashLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wash_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_wash_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}