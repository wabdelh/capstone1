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

data class Baby(
    var name: String? = null,
    var birthDay: Long? = null,

    var foodLog: MutableList<FoodLogObj> = mutableListOf(),
    var washLog: MutableList<WashLogObj> = mutableListOf(),
    var bathroomLog: MutableList<BathroomLogObj> = mutableListOf(),
    var sleepLog: MutableList<SleepLogObj> = mutableListOf(),
    var medLog: MutableList<MedLogObj> = mutableListOf(),

    var Primary: MutableList<String> = mutableListOf(),
    var Secondary: MutableList<String> = mutableListOf(),
    var Tertiary: MutableList<String> = mutableListOf()
)

data class FoodLogObj (
    var kind: String? = null,
    var quantity: Float? = null,
    var time: Long? = null,
    var comment: String? = null
)

data class WashLogObj (
    var product: String? = null,
    var time: Long? = null,
    var comment: String? = null
)

data class BathroomLogObj (
    var kind: String? = null,
    var time: Long? = null,
    var comment: String? = null
)

data class SleepLogObj (
    var start: Long? = null,
    var end: Long? = null,
    var comment: String? = null
)

data class MedLogObj (
    var kind: String? = null,
    var quantity: Float? = null,
    var time: Long? = null,
    var comment: String? = null
)
