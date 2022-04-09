package com.example.capstone

data class Baby(
    var firstName: String? = null,
    var lastName: String? = null,
    var Primary: MutableList<String> = mutableListOf(),
    var Secondary: MutableList<String> = mutableListOf(),
    var Tertiary: MutableList<String> = mutableListOf()
)