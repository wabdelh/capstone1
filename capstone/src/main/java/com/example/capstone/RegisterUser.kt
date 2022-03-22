package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var registerUser: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var editTextFirst: EditText
    private lateinit var editTextLast: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // register button to save user input to the database
        registerUser = findViewById<Button>(R.id.register)
        registerUser.setOnClickListener(this)

        editTextFirst = findViewById(R.id.firstName)
        editTextLast = findViewById(R.id.lastName)
        editTextEmail = findViewById(R.id.emailR)
        editTextPassword = findViewById(R.id.passwordR)

        progressBar = findViewById(R.id.progressBarR)
    }
    override fun onClick(v: View) {
        when (v.id){ //when is the kotlin switch statement. use the following syntax
            R.id.register -> {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val email: String = editTextEmail.text.toString().trim()
        val firstName: String = editTextFirst.text.toString().trim()
        val lastName: String = editTextLast.text.toString().trim()
        val password: String = editTextPassword.text.toString().trim()
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
        if(email.isEmpty()){
            editTextEmail.error = "Email is required!"
            editTextEmail.requestFocus()
            inputInvalid = true

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.error = "Please provide a valid email!"
            editTextEmail.requestFocus()
            inputInvalid = true
        }
        if(password.isEmpty()){
            editTextPassword.error = "Password is required!"
            editTextPassword.requestFocus()
            inputInvalid = true

        }

        if(password.length < 6){
            editTextPassword.error = "Please enter a longer password!"
            editTextPassword.requestFocus()
            inputInvalid = true
        }
        if(inputInvalid)
            return

        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val userData = User(firstName, lastName, email)
                    val database = FirebaseDatabase.getInstance().getReference("Users")
                    val userID = database.push().key

                    if (userID != null) {
                        database.child(userID).setValue(userData).addOnSuccessListener {

                            Toast.makeText(this, "User has been saved successfully!", Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.GONE

                            startActivity(Intent(this, MainActivity::class.java))

                        }.addOnFailureListener{
                            Toast.makeText(this@RegisterUser, "Failed to register!", Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.GONE
                        }
                    }

                }else{
                    Toast.makeText(this@RegisterUser, "This email was used!", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }

            }



    }
}