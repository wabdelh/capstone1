package com.example.capstone


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_after_login_home.*


class AfterLoginHome : AppCompatActivity(), View.OnClickListener {
    // options for drop down
    val options = arrayOf("baby1", "baby2", "baby3", "baby4")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login_home)
        // array adapter needed for options
        val arrayAdapter = ArrayAdapter(
            this@AfterLoginHome,
            android.R.layout.simple_spinner_dropdown_item,
            options
        )
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                Toast.makeText(
                    this@AfterLoginHome,
                    "You Selected " + options[p2],
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        setSupportActionBar(findViewById(R.id.toolbar_main))

        findViewById<Button>(R.id.logout).setOnClickListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.manageUser -> {
                startActivity(Intent(this, ManageUser::class.java))
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
}

