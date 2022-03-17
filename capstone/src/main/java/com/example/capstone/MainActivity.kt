package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
//import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //private var register = findViewById<TextView>(R.id.register)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //register = (TextView) findViewById(R.id.register)
        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.register -> {
                val intent = Intent(this, RegisterUser::class.java)
                startActivity(intent)
            }
        }
    }
}