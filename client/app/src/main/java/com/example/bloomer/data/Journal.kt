package com.example.bloomer.data

data class Journal(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
