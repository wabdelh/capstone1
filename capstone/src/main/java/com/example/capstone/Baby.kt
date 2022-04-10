package com.example.capstone

data class Baby(
    var name: String? = null,

    var foodLog: MutableList<FoodLogObj> = mutableListOf(),

    var Primary: MutableList<String> = mutableListOf(),
    var Secondary: MutableList<String> = mutableListOf(),
    var Tertiary: MutableList<String> = mutableListOf()
)