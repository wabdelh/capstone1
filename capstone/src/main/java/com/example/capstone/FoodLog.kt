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
import com.example.capstone.MyApplication.Companion.babyKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodLog : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_log)

        progressBar = findViewById(R.id.foodLogProgBar)

        loadFoodLog()

        setSupportActionBar(findViewById(R.id.toolbar_food_log))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.food_log, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toAddFoodLog -> {
                startActivity(Intent(this, AddFoodLog::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFoodLog() {
        progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                val baby = d.getValue(Baby::class.java)
                val linearLayout : LinearLayout = findViewById(R.id.linFL)
                for(i in baby?.foodLog!!) { //look for current email in baby
                    val newText = TextView(this@FoodLog)
                    val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                    val dateString = sdf.format(i.time)

                    newText.text = i.quantity.toString() + " grams of " + i.kind + ": " + dateString
                    linearLayout.addView(newText)
                }
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}