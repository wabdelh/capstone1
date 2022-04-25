package com.example.capstone

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Float
import java.lang.NumberFormatException

class AddMedLog : AppCompatActivity(), View.OnClickListener {
    private lateinit var date: DatePicker
    private lateinit var time: TimePicker
    private lateinit var kind: TextView
    private lateinit var quantity: TextView
    private lateinit var comment: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_med_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TimePicker>(R.id.timePicker).setIs24HourView(true)

        kind = findViewById(R.id.kind)
        quantity = findViewById(R.id.quantity)
        date = findViewById(R.id.datePicker)
        time = findViewById(R.id.timePicker)
        comment = findViewById(R.id.medComment)

        findViewById<Button>(R.id.addMedLog).setOnClickListener(this)
    }

    private fun addMedLog() {
        val medKind: String = kind.text.toString().trim()
        val quantityS: String = quantity.text.toString().trim()
        var quantityF = 0.0F
        val time: Long = getTime()
        val commentS: String = comment.text.toString().trim()
        var inputInvalid = false

        try {
            quantityF = Float.parseFloat(quantityS)
        } catch (e: NumberFormatException) {
            quantity.error = "Quantity should be a valid number:"
            quantity.requestFocus()
            inputInvalid = true
        }

        if(medKind.isEmpty()){
            kind.error = "Medicine kind is required."
            kind.requestFocus()
            inputInvalid = true
        }

        if(time > getCurTime()) {
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_LONG).show()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        val medicineData = MedLogObj(medKind, quantityF, time, commentS)

        FirebaseDatabase.getInstance().getReference("Babies").child(MyApplication.babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)

                baby?.medLog?.add(medicineData)
                FirebaseDatabase.getInstance().getReference("Babies").child(MyApplication.babyKey).setValue(baby).addOnSuccessListener {
                    Toast.makeText(this@AddMedLog, "Medicine log has been added successfully!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddMedLog, MedicineLog::class.java))
                }.addOnFailureListener{
                    Toast.makeText(this@AddMedLog, "Failed to add medicine log!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddMedLog, MedicineLog::class.java))
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
            R.id.addMedLog -> {
                addMedLog()
            }
        }
    }

}