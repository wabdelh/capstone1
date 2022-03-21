package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AfterLoginHome : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login_home)

        findViewById<Button>(R.id.logout).setOnClickListener(this)

        findViewById<Button>(R.id.confirmEmailChange).setOnClickListener(this)
        findViewById<Button>(R.id.confirmPasswordChange).setOnClickListener(this)
        findViewById<Button>(R.id.confirmNameChange).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.confirmEmailChange -> {
                changeEmail()
            }
            R.id.confirmPasswordChange -> {
                changePassword()
            }
            R.id.confirmNameChange -> {
                changeName()
            }
        }
    }

    private fun changeEmail() {
        val emailObj = findViewById<EditText>(R.id.emailChange)
        val email: String = emailObj.text.toString().trim()
        val prog = findViewById<ProgressBar>(R.id.mainProgBar)

        if (email.isEmpty()) {
            emailObj.error = "No email provided."
            emailObj.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailObj.error = "Email is invalid."
            emailObj.requestFocus()
            return
        }
        val user = Firebase.auth.currentUser
        prog.visibility = View.VISIBLE
        user!!.updateEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                prog.visibility = View.GONE
                Toast.makeText(this, "Email has been successfully changed", Toast.LENGTH_LONG).show()
                emailObj.setText("")
            }
        }
    }

    private fun changePassword() {
        val passObj = findViewById<EditText>(R.id.passwordChange)
        val pass: String = passObj.text.toString().trim()
        val prog = findViewById<ProgressBar>(R.id.mainProgBar)

        if (pass.isEmpty()) {
            passObj.error = "No password provided"
            passObj.requestFocus()
            return
        }
        if(pass.length < 6) {
            passObj.error = "Password must be six characters or longer"
            passObj.requestFocus()
            return
        }

        val user = Firebase.auth.currentUser
        prog.visibility = View.VISIBLE
        user!!.updatePassword(pass)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                prog.visibility = View.GONE
                Toast.makeText(this, "Password has been successfully changed", Toast.LENGTH_LONG).show()
                passObj.setText("")
            }
        }
    }

    private fun changeName(){
        val fNameObj = findViewById<EditText>(R.id.firstNameChange)
        val fName: String = fNameObj.text.toString().trim()
        val lNameObj = findViewById<EditText>(R.id.lastNameChange)
        val lName: String = lNameObj.text.toString().trim()
        val prog : ProgressBar = findViewById(R.id.mainProgBar)

        if (fName.isEmpty()) {
            fNameObj.error = "No first name provided"
            fNameObj.requestFocus()
        }

        if (lName.isEmpty()) {
            lNameObj.error = "No last name provided"
            lNameObj.requestFocus()
        }

        if(fName.isEmpty() || lName.isEmpty())
            return

        val user = Firebase.auth.currentUser
        prog.visibility = View.VISIBLE

        val userData = User(fName, lName, user?.email)
        val email : String = user?.email as String
        var key = ""
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.orderByChild("email").equalTo(email).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d : DataSnapshot) {
                for (snapshot in d.children) {
                    key = snapshot.key as String
                }
                database.child(key).setValue(userData)
                Toast.makeText(this@AfterLoginHome, "Name has been successfully changed", Toast.LENGTH_LONG).show()
                fNameObj.setText("")
                lNameObj.setText("")
                prog.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}