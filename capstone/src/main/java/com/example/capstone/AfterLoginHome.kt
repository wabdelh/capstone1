package com.example.capstone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.example.capstone.MyApplication.Companion.babyKey
import com.example.capstone.MyApplication.Companion.role
import java.lang.Boolean.getBoolean


class AfterLoginHome : AppCompatActivity(), View.OnClickListener {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login_home)

        progressBar = findViewById(R.id.babyListProgBar)

        setSupportActionBar(findViewById(R.id.toolbar_home))
        findViewById<Button>(R.id.logout).setOnClickListener(this)

        loadBabies()
        //load Dark Mode preferences
        val sharedPref = getSharedPreferences("saveToggle", Context.MODE_PRIVATE)
        val toggle = sharedPref.getBoolean("toggle", false)
        if(toggle) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
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
        progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Babies").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(d: DataSnapshot) {
                for (snapshot in d.children) { //for all babies
                    val baby = snapshot.getValue(Baby::class.java) //store baby data in Baby object
                    if (baby != null) {
                        for(i in baby.Primary) { //look for current email in baby
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                val linearLayout : LinearLayout = findViewById(R.id.linP)

                                val newText = TextView(this@AfterLoginHome)
                                newText.text = baby.name
                                newText.minHeight = 50
                                newText.gravity = Gravity.CENTER
                                newText.setPadding(0,30,0,0)
                                newText.setBackgroundResource(R.drawable.border)
                                newText.height = 100

                                newText.setOnClickListener {
                                    babyKey = snapshot.key.toString()
                                    role = "pri"
                                    startActivity(Intent(this@AfterLoginHome, Main::class.java))
                                }
                                linearLayout.addView(newText)
                                continue
                            }
                        }
                        for(i in baby.Secondary) { //these two are untested, as we need invite implemented to test them
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                val linearLayout : LinearLayout = findViewById(R.id.linS)

                                val newText = TextView(this@AfterLoginHome)
                                newText.text = baby.name
                                newText.minHeight = 50
                                newText.gravity = Gravity.CENTER
                                newText.setPadding(0,30,0,0)
                                newText.setBackgroundResource(R.drawable.border)
                                newText.height = 100

                                newText.setOnClickListener {
                                    babyKey = snapshot.key.toString()
                                    role = "sec"
                                    startActivity(Intent(this@AfterLoginHome, Main::class.java))
                                }

                                linearLayout.addView(newText)
                                continue
                            }
                        }
                        for(i in baby.Tertiary) {
                            if (i == Firebase.auth.currentUser?.email.toString()) {
                                val linearLayout : LinearLayout = findViewById(R.id.linT)

                                val newText = TextView(this@AfterLoginHome)
                                newText.text = baby.name
                                newText.minHeight = 50
                                newText.gravity = Gravity.CENTER
                                newText.setPadding(0,30,0,0)
                                newText.setBackgroundResource(R.drawable.border)
                                newText.height = 100

                                newText.setOnClickListener {
                                    babyKey = snapshot.key.toString()
                                    role = "ter"
                                    startActivity(Intent(this@AfterLoginHome, Main::class.java))
                                }

                                linearLayout.addView(newText)
                                continue
                            }
                        }
                    }
                }
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


}