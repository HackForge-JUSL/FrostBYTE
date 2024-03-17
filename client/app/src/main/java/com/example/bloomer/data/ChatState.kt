package com.example.bloomer.data

import android.graphics.Bitmap
import com.example.bloomer.data.Chat


data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null

)

