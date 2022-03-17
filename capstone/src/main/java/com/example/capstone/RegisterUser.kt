package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RegisterUser : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        findViewById<FloatingActionButton>(R.id.backToLogin).setOnClickListener(this)
        //backToLogin in R.id.backToLogin is the id of the view object (the back button),
        //the <FloatingActionButton> casts the view to whatever kind of object it is,
        //in this context, it is a FloatingActionButton object
        //when this object is clicked, call onClick(v) where v is the object
    }
    override fun onClick(v: View) {
        when (v.id){ //when is the kotlin switch statement. use the following syntax
            R.id.backToLogin -> { //when v.id == R.id.backToLogin, the id of back button object
                //startActivity changes active activity, taking an Intent object made
                //with the name of the page to move to, MainActivity is the activity to move to
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}