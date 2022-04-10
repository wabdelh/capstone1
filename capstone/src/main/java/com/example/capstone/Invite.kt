package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.capstone.MyApplication.Companion.babyKey
import kotlinx.android.synthetic.main.activity_invite.*

class Invite : AppCompatActivity() {
    val roles = arrayOf("Primary","Secondary","Tertiary")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        val arrayAdapter = ArrayAdapter(this@Invite,android.R.layout.simple_spinner_dropdown_item,roles)
        spinner2.adapter = arrayAdapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?,
                                        p1: View?,
                                        p2: Int,
                                        p3: Long) {
              Toast.makeText(this@Invite,"You have selected "+roles[p2],Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        setSupportActionBar(findViewById(R.id.toolbar_invite))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Toast.makeText(this@Invite, babyKey, Toast.LENGTH_SHORT).show()

    }
}