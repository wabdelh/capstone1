package com.example.capstone

import java.sql.Timestamp

data class Baby(
    var name: String? = null,
    var birthDay: Long? = null,

    var foodLog: MutableList<FoodLogObj> = mutableListOf(),

    var Primary: MutableList<String> = mutableListOf(),
    var Secondary: MutableList<String> = mutableListOf(),
    var Tertiary: MutableList<String> = mutableListOf()
)