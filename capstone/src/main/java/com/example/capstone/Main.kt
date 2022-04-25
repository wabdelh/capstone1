package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.example.capstone.MyApplication.Companion.babyKey
import com.example.capstone.MyApplication.Companion.role


class Main : AppCompatActivity(), View.OnClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getName()

        setSupportActionBar(findViewById(R.id.toolbar_main))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.toFoodLog).setOnClickListener(this)
        findViewById<Button>(R.id.toSleepLog).setOnClickListener(this)
        findViewById<Button>(R.id.toWashLog).setOnClickListener(this)
        findViewById<Button>(R.id.toMedLog).setOnClickListener(this)
        findViewById<Button>(R.id.toBathroomLog).setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        when (role) {
            "pri" -> {
            }
            "sec" -> {
                menu?.findItem(R.id.invite)?.isVisible = false
                menu?.findItem(R.id.manageBaby)?.isVisible = false
            }
            "ter" -> {
                menu?.findItem(R.id.invite)?.isVisible = false
                menu?.findItem(R.id.manageBaby)?.isVisible = false
            }
            else -> {
                Toast.makeText(this, "ERROR: Invalid role.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java))
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.invite -> {
                startActivity(Intent(this, Invite::class.java))
                return true
            }
            R.id.manageBaby -> {
                startActivity(Intent(this, ManageBaby::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getName() {
        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java) //store baby data in Baby object
                if (baby != null) {
                    supportActionBar?.title = baby.name
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toFoodLog -> {
                startActivity(Intent(this, FoodLog::class.java))
            }

            R.id.toMedLog -> {
                startActivity(Intent(this, MedicineLog::class.java))
            }

            R.id.toWashLog -> {
                startActivity(Intent(this, WashLog::class.java))
            }

            R.id.toSleepLog -> {
                startActivity(Intent(this, SleepLog::class.java))
            }

            R.id.toBathroomLog -> {
                startActivity(Intent(this, BathroomLog::class.java))
            }
        }
    }

}


