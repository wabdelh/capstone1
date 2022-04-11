package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.capstone.MyApplication.Companion.babyKey
import kotlinx.android.synthetic.main.activity_invite.*

class Invite : AppCompatActivity(), View.OnClickListener {
    val roles = arrayOf("Primary","Secondary","Tertiary")
    lateinit var invite: Button
    var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        findViewById<TextView>(R.id.Invite).setOnClickListener(this)
        invite = findViewById(R.id.Invite)
        invite.setOnClickListener(this)

        val arrayAdapter = ArrayAdapter(this@Invite,android.R.layout.simple_spinner_dropdown_item,roles)
        spinner2.adapter = arrayAdapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?,
                                        p1: View?,
                                        p2: Int,
                                        p3: Long) {

              Toast.makeText(this@Invite,"You have selected "+roles[p2],Toast.LENGTH_LONG).show()
                num = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        setSupportActionBar(findViewById(R.id.toolbar_invite))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Toast.makeText(this@Invite, babyKey, Toast.LENGTH_SHORT).show()

    }

    override fun onClick(v: View) {
      when(v.id) {R.id.Invite ->{

          // add user as primary caretaker
          if(num == 0){



          }
          // add user as secondary caretaker
          if(num == 1){



          }
          // add user as tertiary caretaker
          if(num == 2){



          }


      }

      }
    }


}

