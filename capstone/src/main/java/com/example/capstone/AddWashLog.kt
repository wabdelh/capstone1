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

class AddWashLog : AppCompatActivity(), View.OnClickListener {
    private lateinit var date: DatePicker
    private lateinit var time: TimePicker
    private lateinit var kind: TextView
    private lateinit var comment: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wash_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_wash_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<TimePicker>(R.id.timePicker).setIs24HourView(true)

        kind = findViewById(R.id.kind)
        date = findViewById(R.id.datePicker)
        time = findViewById(R.id.timePicker)
        comment = findViewById(R.id.washComment)

        findViewById<Button>(R.id.addWashLog).setOnClickListener(this)
    }

    private fun addWashLog() {
        val product: String = kind.text.toString().trim()
        val time: Long = getTime()
        val commentS: String = comment.text.toString().trim()
        var inputInvalid = false

        if(product.isEmpty()){
            kind.error = "Product used is required."
            kind.requestFocus()
            inputInvalid = true
        }

        if(time > getCurTime()) {
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_LONG).show()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        val washData = WashLogObj(product, time, commentS)

        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)

                baby?.washLog?.add(washData)
                FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).setValue(baby).addOnSuccessListener {
                    Toast.makeText(this@AddWashLog, "Bathe log has been added successfully!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddWashLog, WashLog::class.java))
                }.addOnFailureListener{
                    Toast.makeText(this@AddWashLog, "Failed to add Bathe log!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddWashLog, WashLog::class.java))
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
            R.id.addWashLog -> {
                addWashLog()
            }
        }
    }
}