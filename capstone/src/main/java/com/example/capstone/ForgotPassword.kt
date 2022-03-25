package com.example.capstone

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<TextView>(R.id.submitButton).setOnClickListener(this)
    }

    override fun onClick(v : View) {
        when(v.id) {
            R.id.submitButton -> {
                val editEmail = findViewById<TextView>(R.id.email)
                val email: String = editEmail.text.toString().trim { it <= ' '}
                if (email.isEmpty()){
                    Toast.makeText(this@ForgotPassword, "Please enter email address.", Toast.LENGTH_SHORT).show()
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener{task ->
                            if(task.isSuccessful){
                                Toast.makeText(this@ForgotPassword, "Successfully sent email.", Toast.LENGTH_SHORT).show()

                                finish()
                            }else{
                                Toast.makeText(this@ForgotPassword, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }


}

