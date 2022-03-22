package com.example.capstone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var loginUser: Button
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("test")
        findViewById<TextView>(R.id.register).setOnClickListener(this)
        findViewById<TextView>(R.id.login).setOnClickListener(this)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


        // login button to verify user input to the database
        loginUser = findViewById(R.id.login)
        loginUser.setOnClickListener(this)

        editEmail = findViewById(R.id.emailL)
        editPassword = findViewById(R.id.passwordL)
        progressBar = findViewById(R.id.progressBar)

    }

    override fun onClick(v: View) {
        when (v.id) { //when is the kotlin switch statement. use the following syntax
            R.id.register -> { //when v.id == R.id.register, the id of register object
                //startActivity changes active activity, taking an Intent object made
                //with the name of the page to move to, RegisterUser is the activity to move to
                startActivity(Intent(this, RegisterUser::class.java))
            }
            R.id.login -> { //when v.id == R.id.register, the id of register object
                //startActivity changes active activity, taking an Intent object made
                //with the name of the page to move to, RegisterUser is the activity to move to
                loginUser()
            }
        }
    }

    private fun loginUser() {

        // var for username and pw
        val email: String = editEmail.text.toString().trim()
        val password: String = editPassword.text.toString().trim()
        var inputInvalid = false

        if (email.isEmpty()) {
            editEmail.error = "Email is required!"
            editEmail.requestFocus()
            inputInvalid = true
        }

        if (password.isEmpty()) {
            editPassword.error = "Password is required!"
            editPassword.requestFocus()
            inputInvalid = true
        }
        if(inputInvalid)
            return

        progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this, AfterLoginHome::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Login failed, please confirm credentials.",
                        Toast.LENGTH_SHORT

                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
    }
}


