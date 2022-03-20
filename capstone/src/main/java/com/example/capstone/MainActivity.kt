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


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var progressBar: ProgressBar
    lateinit var loginUser: Button
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.register).setOnClickListener(this)
        findViewById<TextView>(R.id.login).setOnClickListener(this)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


        // login button to verify user input to the database
        loginUser = findViewById<Button>(R.id.login)
        loginUser.setOnClickListener(this)

        editEmail = findViewById<EditText>(R.id.emailL)
        editPassword = findViewById<EditText>(R.id.passwordL)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

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

    fun loginUser() {

        // var for username and pw
        var email: String = editEmail.text.toString().trim()
        var password: String = editPassword.text.toString().trim()

        if (email.isEmpty()) {
            editEmail.setError("Email is required!")
            editEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required!")
            editPassword.requestFocus()
            return
        }

        progressBar.setVisibility(View.VISIBLE)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    setContentView(R.layout.activity_after_login_home)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Login failed, please confirm credentials.",
                        Toast.LENGTH_SHORT

                    ).show()
                    progressBar.setVisibility(View.GONE)
                }
            }
    }
}


