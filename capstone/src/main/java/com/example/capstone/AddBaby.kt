package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.sql.Date

class AddBaby : AppCompatActivity(), View.OnClickListener {
    private lateinit var babyName: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var date: DatePicker
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_baby)

        setSupportActionBar(findViewById(R.id.toolbar_add_baby))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        babyName = findViewById(R.id.babyName)
        progressBar = findViewById(R.id.progressBarB)
        date = findViewById(R.id.datePicker)

        findViewById<Button>(R.id.inviteB).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) { //when is the kotlin switch statement. use the following syntax
            R.id.inviteB -> {
                addBaby()
            }
        }
    }

    private fun addBaby() {
        val name: String = babyName.text.toString().trim()

        val day = date.dayOfMonth
        val month = date.month
        val year = date.year - 1900
        val bDate = Date(year, month, day)
        val timeVal = bDate.time

        var inputInvalid = false
        if(name.isEmpty()){
            babyName.error = "First name is required!"
            babyName.requestFocus()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        progressBar.visibility = View.VISIBLE
        val fList : MutableList<FoodLogObj> = mutableListOf()
        val wList : MutableList<WashLogObj> = mutableListOf()
        val sList : MutableList<SleepLogObj> = mutableListOf()
        val mList : MutableList<MedLogObj> = mutableListOf()
        val bList : MutableList<BathroomLogObj> = mutableListOf()

        val priList : MutableList<String> = mutableListOf(auth.currentUser?.email.toString())
        val secList : MutableList<String> = mutableListOf()
        val terList : MutableList<String> = mutableListOf()

        val babyData = Baby(name, timeVal, fList, wList, bList, sList, mList, priList, secList, terList)
        val database = FirebaseDatabase.getInstance().getReference("Babies")
        val babyID = database.push().key

        if (babyID != null) {
            database.child(babyID).setValue(babyData).addOnSuccessListener {

                Toast.makeText(this, "Baby has been saved successfully!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE

                startActivity(Intent(this, AfterLoginHome::class.java))

            }.addOnFailureListener{
                Toast.makeText(this, "Failed to add baby!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        }
    }
}