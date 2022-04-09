package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddBaby : AppCompatActivity(), View.OnClickListener {
    private lateinit var editTextFirst: EditText
    private lateinit var editTextLast: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_baby)

        setSupportActionBar(findViewById(R.id.toolbar_add_baby))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        editTextFirst = findViewById(R.id.babyFirstName)
        editTextLast = findViewById(R.id.babyLastName)
        progressBar = findViewById(R.id.progressBarB)

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
        val firstName: String = editTextFirst.text.toString().trim()
        val lastName: String = editTextLast.text.toString().trim()

        var inputInvalid = false
        if(firstName.isEmpty()){
            editTextFirst.error = "First name is required!"
            editTextFirst.requestFocus()
            inputInvalid = true
        }

        if(lastName.isEmpty()){
            editTextLast.error = "Last name is required!"
            editTextLast.requestFocus()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        progressBar.visibility = View.VISIBLE
        val priList : MutableList<String> = mutableListOf(auth.currentUser?.email.toString())
        val secList : MutableList<String> = mutableListOf()
        val terList : MutableList<String> = mutableListOf()

        val babyData = Baby(firstName, lastName, priList, secList, terList)
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