package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

import com.example.capstone.MyApplication.Companion.babyKey
import java.sql.Date

class ManageBaby : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_baby)

        setSupportActionBar(findViewById(R.id.toolbar_main))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.confirmDateChange).setOnClickListener(this)
        findViewById<Button>(R.id.confirmNameChange).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confirmDateChange -> {
                changeDate()
            }
            R.id.confirmNameChange -> {
                changeName()
            }
        }
    }

    private fun changeDate(){
        val date: DatePicker = findViewById(R.id.datePicker)
        val prog : ProgressBar = findViewById(R.id.babyListProgBar)

        val day = date.dayOfMonth
        val month = date.month
        val year = date.year - 1900
        val bDate = Date(year, month, day)
        val timeVal = bDate.time

        prog.visibility = View.VISIBLE

        val temp = FirebaseDatabase.getInstance().getReference("Babies").child(babyKey)
        temp.ref.child("birthDay").setValue(timeVal)
        Toast.makeText(this, "Name has been successfully changed", Toast.LENGTH_SHORT).show()
        prog.visibility = View.GONE
    }

    private fun changeName(){
        val name : EditText = findViewById(R.id.nameChange)
        val changeName: String = name.text.toString().trim()
        val prog : ProgressBar = findViewById(R.id.babyListProgBar)
        var inputInvalid = false

        if (changeName.isEmpty()) {
            name.error = "No first name provided"
            name.requestFocus()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        prog.visibility = View.VISIBLE

        val temp = FirebaseDatabase.getInstance().getReference("Babies").child(babyKey)
        temp.ref.child("name").setValue(changeName)
        Toast.makeText(this, "Name has been successfully changed", Toast.LENGTH_SHORT).show()
        name.setText("")
        prog.visibility = View.GONE
    }
}
