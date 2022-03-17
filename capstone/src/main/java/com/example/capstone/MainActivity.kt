package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.register).setOnClickListener(this)
        //register in R.id.register is the id of the view object (the register button),
        //the <TextView> casts the view to whatever kind of object it is,
        //in this context, it is a TextView objects
        //when this object is clicked, call onClick(v) where v is the object
    }
    override fun onClick(v: View) {
        when (v.id) { //when is the kotlin switch statement. use the following syntax
            R.id.register -> { //when v.id == R.id.register, the id of register object
                //startActivity changes active activity, taking an Intent object made
                //with the name of the page to move to, RegisterUser is the activity to move to
                startActivity(Intent(this, RegisterUser::class.java))
            }
        }
    }
}