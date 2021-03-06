package com.example.capstone


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.capstone.MyApplication.Companion.babyKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_invite.*

class Invite : AppCompatActivity(), View.OnClickListener {
    val roles = arrayOf("Primary", "Secondary", "Tertiary")
    lateinit var invite: Button
    private lateinit var iemail: EditText
    private lateinit var auth: FirebaseAuth
    var num = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        findViewById<TextView>(R.id.Invite).setOnClickListener(this)
        iemail = findViewById(R.id.Iemail)

        invite = findViewById(R.id.Invite)
        invite.setOnClickListener(this)

        auth = Firebase.auth

        val arrayAdapter =
            ArrayAdapter(this@Invite, android.R.layout.simple_spinner_dropdown_item, roles)
        spinner2.adapter = arrayAdapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

                //Toast.makeText(this@Invite, "You have selected " + roles[p2], Toast.LENGTH_LONG)
                //    .show()
                num = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        setSupportActionBar(findViewById(R.id.toolbar_invite))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Toast.makeText(this@Invite, babyKey, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.Invite -> {
                if(validEmail())
                    addBabyEmail(num)
            }
        }
    }

    private fun validEmail(): Boolean {
        val email: String = iemail.text.toString().trim()

        if (email.isEmpty()) {
            iemail.error = "Email is required!"
            iemail.requestFocus()
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            iemail.error = "Please provide a valid email!"
            iemail.requestFocus()
            return false;
        }
        return true
    }

    private fun addBabyEmail(mode : Int) {
        val email: String = iemail.text.toString().trim()
        FirebaseDatabase.getInstance().getReference("Babies").child(babyKey)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(d: DataSnapshot) {
                    val baby = d.getValue(Baby::class.java)
                    when(mode) { //add email to baby list depending on num, which selects user type
                        0 -> baby?.Primary?.add(email) //if primary selected, add email to primary list
                        1 -> baby?.Secondary?.add(email) //if secondary selected, add email to secondary list
                        2 -> baby?.Tertiary?.add(email) //if tertiary selected, add email to secondary list
                    }
                    FirebaseDatabase.getInstance().getReference("Babies").child(babyKey)
                        .setValue(baby).addOnSuccessListener {
                            Toast.makeText(this@Invite, "Primary Email has been added successfully!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Invite, Main::class.java))
                        }.addOnFailureListener {
                            Toast.makeText(this@Invite, "Failed to add Email", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Invite, Main::class.java))
                        }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}