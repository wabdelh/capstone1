package com.example.capstone

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

import com.example.capstone.MyApplication.Companion.babyKey
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.sql.Date

class ManageBaby : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_baby)

        setSupportActionBar(findViewById(R.id.toolbar_main))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.confirmDateChange).setOnClickListener(this)
        findViewById<Button>(R.id.confirmNameChange).setOnClickListener(this)
        findViewById<Button>(R.id.deleteBaby).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confirmDateChange -> {
                changeDate()
            }
            R.id.confirmNameChange -> {
                changeName()
            }
            R.id.deleteBaby -> {
                deleteBaby()
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
    private fun deleteBaby(){

        // User Deletion requires that the user has recently signed in,
        // so re-authentication is required.
        val database = FirebaseDatabase.getInstance().getReference("Babies").child(babyKey)
        var key = ""

        val user = Firebase.auth.currentUser!!
        val builder = AlertDialog.Builder(this) // build a prompt for password confirmation
        val inflater = layoutInflater
        builder.setTitle("Confirm Your Password")
        val dialogLayout = inflater.inflate(R.layout.user_delete_promt, null)
        val editTextObj : EditText = dialogLayout.findViewById(R.id.confirmPassword)
        val userEmailString : String = user.email.toString().trim() // get current user's email for later use

        builder.setView(dialogLayout)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { builder, which ->
            val credential = EmailAuthProvider
                .getCredential(userEmailString, editTextObj.text.toString())
            user.reauthenticate(credential) // user need to be re-authenticated before deletion
                .addOnCompleteListener(this){task ->
                                if (task.isSuccessful) {
                                    // query for current user's email
                                    database.orderByChild("email").equalTo(user.email as String).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(d: DataSnapshot) {
                                            for (snapshot in d.children) {
                                                key = snapshot.key as String // gets key of data
                                            }
                                            database.removeValue().addOnSuccessListener { // delete in Realtime Database
                                                Toast.makeText(this@ManageBaby, "Baby has been successfully deleted.", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        override fun onCancelled(error: DatabaseError) {} //required
                                    })
                                    Toast.makeText(baseContext, "Baby has been successfully deleted.", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, Login::class.java))
                                }
                                else{
                                    Toast.makeText(baseContext, "Re-auth failed, please confirm credentials.", Toast.LENGTH_SHORT).show()
                                }


                    }


        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { builder, which -> builder.cancel() })
        builder.show()

    }
}
