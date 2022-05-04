package com.example.capstone

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ManageUser : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)

        setSupportActionBar(findViewById(R.id.toolbar_man))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.confirmEmailChange).setOnClickListener(this)
        findViewById<Button>(R.id.confirmNameChange).setOnClickListener(this)
        findViewById<Button>(R.id.deleteUser).setOnClickListener(this)

        // Declare the switch from the layout file
        val btn = findViewById<Switch>(R.id.switch1)

        // set the switch to listen on checked change
        btn.setOnCheckedChangeListener { _, isChecked ->

            if (btn.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                btn.text = "Disable dark mode"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                btn.text = "Enable dark mode"
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, Login::class.java))
            }
            R.id.confirmEmailChange -> {
                changeEmail()
            }
            R.id.confirmNameChange -> {
                changeName()
            }
            R.id.deleteUser -> {
                deleteUser()
            }
        }

    }

    private fun changeEmail() {
        val emailObj: EditText = findViewById(R.id.emailChange)
        val email: String = emailObj.text.toString().trim()
        val prog: ProgressBar = findViewById(R.id.babyListProgBar)
        var key = ""
        val database = FirebaseDatabase.getInstance().getReference("Users")
        var u = User()

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

        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener { // Query the database using entered email
            override fun onDataChange(snapshot: DataSnapshot){
                if (snapshot.exists()){ // Entered email exists in the database
                    emailObj.error = "This email was used"
                    emailObj.requestFocus()
                    return
                } else{
                    val user = Firebase.auth.currentUser
                    prog.visibility = View.VISIBLE
                    user!!.updateEmail(email)

                    database.orderByChild("email").equalTo(user.email as String).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(d: DataSnapshot) {
                            for (snapshot in d.children) {
                                key = snapshot.key as String //gets key of data
                                u = snapshot.getValue(User::class.java)!! //this puts the value with a given email into the User class
                            }
                            u.email = email //change the email
                            database.child(key).setValue(u).addOnSuccessListener { //this is run after the key
                                Toast.makeText(this@ManageUser, "Email has been successfully changed", Toast.LENGTH_LONG).show()
                                emailObj.setText("")
                                prog.visibility = View.GONE
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {} //required
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {} //required
        })
    }

    private fun changeName(){
        val fNameObj : EditText = findViewById(R.id.firstNameChange)
        val fName: String = fNameObj.text.toString().trim()
        val lNameObj : EditText = findViewById(R.id.lastNameChange)
        val lName: String = lNameObj.text.toString().trim()
        val prog : ProgressBar = findViewById(R.id.babyListProgBar)
        var inputInvalid = false

        if (fName.isEmpty()) {
            fNameObj.error = "No first name provided"
            fNameObj.requestFocus()
            inputInvalid = true
        }

        if (lName.isEmpty()) {
            lNameObj.error = "No last name provided"
            lNameObj.requestFocus()
            inputInvalid = true
        }

        if(inputInvalid)
            return

        val user = Firebase.auth.currentUser
        prog.visibility = View.VISIBLE

        val userData = User(fName, lName, user?.email)
        val email : String = user?.email as String
        var key = ""
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d : DataSnapshot) {
                for (snapshot in d.children) {
                    key = snapshot.key as String
                }
                database.child(key).setValue(userData).addOnSuccessListener { //why does this have to be in the onDataChange? It does
                    Toast.makeText(this@ManageUser, "Name has been successfully changed", Toast.LENGTH_LONG).show()
                    fNameObj.setText("")
                    lNameObj.setText("")
                    prog.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun deleteUser(){
        // User Deletion requires that the user has recently signed in,
        // so re-authentication is required.
        val uDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val bDatabase = FirebaseDatabase.getInstance().getReference("Babies")
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
                        user.delete() // delete in Authentication
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // query for current user's email
                                    // in the future, look for babies with email
                                    uDatabase.orderByChild("email").equalTo(user.email as String).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(d: DataSnapshot) {
                                            for (snapshot in d.children) {
                                                key = snapshot.key as String // gets key of data
                                            }
                                            uDatabase.child(key).removeValue().addOnSuccessListener { // delete in Realtime Database
                                                Toast.makeText(this@ManageUser, "User has successfully been deleted", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        override fun onCancelled(error: DatabaseError) {} //required
                                    })
                                    Toast.makeText(baseContext, "User Deleted Successfully!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, Login::class.java))
                                }
                            }

                    } else{
                        Toast.makeText(baseContext, "Re-auth failed, please confirm credentials.", Toast.LENGTH_SHORT).show()
                    }
                }

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { builder, which -> builder.cancel() })
        builder.show()

    }
}