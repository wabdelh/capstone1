package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.example.capstone.MyApplication.Companion.babyKey


class AfterLoginHome : AppCompatActivity(), View.OnClickListener {
    //private lateinit var babyList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login_home)

        setSupportActionBar(findViewById(R.id.toolbar_home))

        loadBabies()

        findViewById<Button>(R.id.logout).setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.manageUser -> {
                startActivity(Intent(this, ManageUser::class.java))
                return true
            }
            R.id.addBabyI -> {
                startActivity(Intent(this, AddBaby::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this, Login::class.java))
            }
        }
    }

    private fun loadBabies() {
        FirebaseDatabase.getInstance().getReference("Babies").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(d: DataSnapshot) {
                for (snapshot in d.children) {
                    val baby = snapshot.getValue(Baby::class.java)
                    if (baby != null) {
                        for(i in baby.Primary) {
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                val linearLayout : LinearLayout = findViewById(R.id.linP)

                                val newText = TextView(this@AfterLoginHome)
                                newText.text = baby.firstName + " " + baby.lastName
                                newText.setOnClickListener {
                                    babyKey = snapshot.key.toString()
                                    startActivity(Intent(this@AfterLoginHome, Main::class.java))
                                }

                                linearLayout.addView(newText)
                            }
                        }
                        for(i in baby.Secondary) {
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                //add baby to list
                            }
                        }
                        for(i in baby.Tertiary) {
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                //add baby to list
                            }
                        }
                        //key = snapshot.key as String //gets key of data
                        //u = snapshot.getValue(User::class.java)!! //this puts the value with a given email into the User class

                    }
                }
                //u.email = email //change the email
                //database.child(key).setValue(u).addOnSuccessListener { //this is run after the key
                    //Toast.makeText(this@ManageUser, "Name has been successfully changed", Toast.LENGTH_LONG).show()
//                    prog.visibility = View.GONE
                //}
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        /*database.orderByChild("email").equalTo(user.email as String).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                for (snapshot in d.children) {
                    key = snapshot.key as String //gets key of data
                    u = snapshot.getValue(User::class.java)!! //this puts the value with a given email into the User class
                }
                u.email = email //change the email
                database.child(key).setValue(u).addOnSuccessListener { //this is run after the key
                    Toast.makeText(this@ManageUser, "Name has been successfully changed", Toast.LENGTH_LONG).show()
                    emailObj.setText("")
                    prog.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {} //required
        })*/
    }
}