package com.example.capstone

import android.app.Application
class MyApplication: Application() {
    companion object {
        var babyKey : String = ""
        var role : String = ""
    }
}

//to import global data use:
//import com.example.capstone.MyApplication.Companion.babyKey
//or right click on babyKey and click import.