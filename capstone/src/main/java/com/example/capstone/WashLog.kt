package com.example.capstone

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.capstone.MyApplication.Companion.babyKey
import com.example.capstone.MyApplication.Companion.role
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WashLog : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wash_log)

        progressBar = findViewById(R.id.washLogProgBar)

        loadWashLog()

        setSupportActionBar(findViewById(R.id.toolbar_wash_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.log, menu)

        when (role) {
            "pri" -> {
            }
            "sec" -> {
            }
            "ter" -> {
                menu?.findItem(R.id.addLog)?.isVisible = false
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
            R.id.addLog -> {
                startActivity(Intent(this, AddWashLog::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadWashLog() {
        progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)
                val linearLayout : LinearLayout = findViewById(R.id.linFL)
                for(i in baby?.washLog!!) { //look for current email in baby
                    val newText = TextView(this@WashLog)
                    val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                    val dateString = sdf.format(i.time)

                    newText.text = i.product + ": " + dateString + ". Comment: " + i.comment
                    linearLayout.addView(newText)
                }
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}