package com.example.capstone

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

class AddFoodLog : AppCompatActivity(), View.OnClickListener {
    private lateinit var date: DatePicker
    private lateinit var time: TimePicker
    private lateinit var kind: TextView
    private lateinit var quantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_log)

        setSupportActionBar(findViewById(R.id.toolbar_add_food_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        kind = findViewById(R.id.kind)
        quantity = findViewById(R.id.quantity)
        date = findViewById(R.id.datePicker)
        time = findViewById(R.id.timePicker)

        findViewById<Button>(R.id.addFoodLog).setOnClickListener(this)
    }

    private fun addFoodLog() {
        val foodKind: String = kind.text.toString().trim()
        val quantityS: String = quantity.text.toString().trim()
        var quantityF = 0.0F
        val time: Long = getTime()

        var inputInvalid = false

        try {
            quantityF = parseFloat(quantityS)
        } catch (e: NumberFormatException) {
            quantity.error = "Quantity should be a valid number:"
            quantity.requestFocus()
            inputInvalid = true
        }

        if(foodKind.isEmpty()){
            kind.error = "Food kind is required."
            kind.requestFocus()
            inputInvalid = true
        }

        if(time > getCurTime()) {
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_LONG).show()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        val foodData = FoodLogObj(foodKind, quantityF, time)

        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)

                baby?.foodLog?.add(foodData)
                FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).setValue(baby).addOnSuccessListener {
                    Toast.makeText(this@AddFoodLog, "Food log has been added successfully!", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(this@AddFoodLog, "Failed to add food log!", Toast.LENGTH_LONG).show()
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
            R.id.addFoodLog -> {
                addFoodLog()
            }
        }
    }
}

