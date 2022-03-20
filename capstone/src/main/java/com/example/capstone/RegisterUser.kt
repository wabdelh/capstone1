package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        editTextFirst = findViewById<EditText>(R.id.firstName)
        editTextLast = findViewById<EditText>(R.id.lastName)
        editTextEmail = findViewById<EditText>(R.id.emailR)
        editTextPassword = findViewById<EditText>(R.id.passwordR)

        progressBar = findViewById<ProgressBar>(R.id.progressBarR)


        findViewById<FloatingActionButton>(R.id.backToLogin).setOnClickListener(this)
        //backToLogin in R.id.backToLogin is the id of the view object (the back button),
        //the <FloatingActionButton> casts the view to whatever kind of object it is,
        //in this context, it is a FloatingActionButton object
        //when this object is clicked, call onClick(v) where v is the object
    }
    override fun onClick(v: View) {
        when (v.id){ //when is the kotlin switch statement. use the following syntax
            R.id.backToLogin -> { //when v.id == R.id.backToLogin, the id of back button object
                //startActivity changes active activity, taking an Intent object made
                //with the name of the page to move to, MainActivity is the activity to move to
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.register -> {
                registerUser()
            }
        }
    }

    fun registerUser() {
        var email: String = editTextEmail.text.toString().trim()
        var firstName: String = editTextFirst.text.toString().trim()
        var lastName: String = editTextLast.text.toString().trim()
        var password: String = editTextPassword.text.toString().trim()

        if(firstName.isEmpty()){
            editTextFirst.setError("First name is required!")
            editTextFirst.requestFocus()
            return

        }

        if(lastName.isEmpty()){
            editTextLast.setError("Last name is required!")
            editTextLast.requestFocus()
            return

        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!")
            editTextEmail.requestFocus()
            return

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email!")
            editTextEmail.requestFocus()
            return
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required!")
            editTextPassword.requestFocus()
            return

        }

        if(password.length < 6){
            editTextPassword.setError("Please enter a longer password!")
            editTextPassword.requestFocus()
            return
        }

        progressBar.setVisibility(View.VISIBLE)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val userData = User(firstName, lastName, email)
                    val database = FirebaseDatabase.getInstance().getReference("Users")
                    val userID = database.push().key

                    if (userID != null) {
                        database.child(userID).setValue(userData).addOnSuccessListener {

                            Toast.makeText(this, "User has been saved successfully!", Toast.LENGTH_LONG).show()
                            progressBar.setVisibility(View.GONE)

                            // redirect to Login Layout here

                        }.addOnFailureListener{
                            Toast.makeText(this@RegisterUser, "Failed to register!", Toast.LENGTH_LONG).show()
                            progressBar.setVisibility(View.GONE)
                        }
                    }

                }else{
                    Toast.makeText(this@RegisterUser, "This email was used!", Toast.LENGTH_LONG).show()
                    progressBar.setVisibility(View.GONE)
                }

            }



    }
}