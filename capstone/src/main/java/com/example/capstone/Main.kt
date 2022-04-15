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
import java.sql.Date

class Main : AppCompatActivity(), View.OnClickListener  {
    lateinit var birth: Date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getName()

        setSupportActionBar(findViewById(R.id.toolbar_main))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.toFoodLog).setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)

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
        }
    }

}


