package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AfterLoginHome : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login_home)
        findViewById<Button>(R.id.logout).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> {
                // Firebase.auth.signOut()
                //println("1")
                // startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}