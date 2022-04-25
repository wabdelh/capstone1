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

class AddBathroomLog : AppCompatActivity(), View.OnClickListener {
    private lateinit var date: DatePicker
    private lateinit var time: TimePicker
    private lateinit var kind: TextView
    private lateinit var comment: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bathroom_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_bathroom_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TimePicker>(R.id.timePicker).setIs24HourView(true)

        kind = findViewById(R.id.kind)
        date = findViewById(R.id.datePicker)
        time = findViewById(R.id.timePicker)
        comment = findViewById(R.id.bathroomComment)

        findViewById<Button>(R.id.addBathroomLog).setOnClickListener(this)
    }

    private fun addBathroomLog() {
        val bathroomKind: String = kind.text.toString().trim()
        val time: Long = getTime()
        val commentS: String = comment.text.toString().trim()
        var inputInvalid = false

        // idk how to word this
        if(bathroomKind.isEmpty()){
            kind.error = "Bathroom kind is required."
            kind.requestFocus()
            inputInvalid = true
        }

        if(time > getCurTime()) {
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_LONG).show()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        val bathroomData = BathroomLogObj(bathroomKind, time, commentS)

        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)

                baby?.bathroomLog?.add(bathroomData)
                FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).setValue(baby).addOnSuccessListener {
                    Toast.makeText(this@AddBathroomLog, "Bathroom log has been added successfully!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddBathroomLog, BathroomLog::class.java))
                }.addOnFailureListener{
                    Toast.makeText(this@AddBathroomLog, "Failed to add bathroom log!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddBathroomLog, BathroomLog::class.java))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month, date.dayOfMonth, time.hour, time.minute, 0)
        return calendar.timeInMillis
    }

    private fun getCurTime(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    override fun onClick(v: View) {
        when (v.id) { //when is the kotlin switch statement. use the following syntax
            R.id.addBathroomLog -> {
                addBathroomLog()
            }
        }
    }
}