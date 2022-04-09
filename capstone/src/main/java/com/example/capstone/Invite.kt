package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.MyApplication.Companion.babyKey

class Invite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        setSupportActionBar(findViewById(R.id.toolbar_invite))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Toast.makeText(this@Invite, babyKey, Toast.LENGTH_SHORT).show()

    }
}