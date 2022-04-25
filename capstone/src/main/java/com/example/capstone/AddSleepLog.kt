package com.example.capstone

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.capstone.MyApplication.Companion.babyKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Float.parseFloat
import java.lang.NumberFormatException

class AddSleepLog : AppCompatActivity(), View.OnClickListener {
    private lateinit var startDate: DatePicker
    private lateinit var startTime: TimePicker
    private lateinit var endDate: DatePicker
    private lateinit var endTime: TimePicker
    private lateinit var comment: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sleep_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_sleep_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TimePicker>(R.id.timePickerStart).setIs24HourView(true)
        findViewById<TimePicker>(R.id.timePickerEnd).setIs24HourView(true)

        startDate = findViewById(R.id.datePickerStart)
        startTime = findViewById(R.id.timePickerStart)
        endDate = findViewById(R.id.datePickerEnd)
        endTime = findViewById(R.id.timePickerEnd)
        comment = findViewById(R.id.sleepComment)

        findViewById<Button>(R.id.addSleepLog).setOnClickListener(this)
    }

    private fun addSleepLog() {
        val start: Long = getTimeStart()
        val end: Long = getTimeEnd()
        val commentS: String = comment.text.toString().trim()
        var inputInvalid = false

        //if(start > getCurTime() || end > getCurTime()) {
        //    Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_LONG).show()
        //    inputInvalid = true
        // }

        if(inputInvalid)
            return

        val sleepData = SleepLogObj(start, end, commentS)

        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)

                baby?.sleepLog?.add(sleepData)
                FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).setValue(baby).addOnSuccessListener {
                    Toast.makeText(this@AddSleepLog, "Sleep log has been added successfully!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddSleepLog, SleepLog::class.java))
                }.addOnFailureListener{
                    Toast.makeText(this@AddSleepLog, "Failed to add Sleep log!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddSleepLog, SleepLog::class.java))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getTimeStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(startDate.year, startDate.month, startDate.dayOfMonth, startTime.hour, startTime.minute, 0)
        return calendar.timeInMillis
    }

    private fun getTimeEnd(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(endDate.year, endDate.month, endDate.dayOfMonth, endTime.hour, endTime.minute, 0)
        return calendar.timeInMillis
    }

    private fun getCurTime(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    override fun onClick(v: View) {
        when (v.id) { //when is the kotlin switch statement. use the following syntax
            R.id.addSleepLog -> {
                addSleepLog()
            }
        }
    }
}