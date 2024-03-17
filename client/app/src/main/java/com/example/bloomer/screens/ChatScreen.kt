package com.example.bloomer.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.ChatUiEvent
import com.example.bloomer.ViewModels.MessageViewModel
import com.example.bloomer.R
import com.example.bloomer.Screen
import com.example.bloomer.data.Injection
import com.example.bloomer.data.Message
import com.example.bloomer.data.Result
import com.example.bloomer.data.UserRepository
import com.example.bloomer.data.items1
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    roomId: String,
    messageViewModel:
    MessageViewModel = viewModel(),
    navController: NavController,
) {
    val messages by messageViewModel.messages.observeAsState(emptyList())
    messageViewModel.setRoomId(roomId)
    val text = remember { mutableStateOf("") }

    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        topBar = {
            AppBarView(
                fontSize = 30.sp,
                stringResourceId = R.string.chatroom_screen_app_bar,
            ) { navController.navigateUp()}
        },
        bottomBar = {
            NavigationBar {
                items1.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex1 == index,
                        onClick = {
                            selectedItemIndex1 = index
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex1) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        })
    {
        Box(modifier = Modifier.padding(it)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Display the chat messages
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) { message ->
                        ChatMessageItem(
                            message = message.copy(
                                isSentByCurrentUser
                                = message.senderId == messageViewModel.currentUser.value?.email
                            )
                        )
                    }
                }

                // Chat input field and send icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        modifier = Modifier
                            .weight(1f),
                        value = text.value,
                        onValueChange = { text.value = it},
                        placeholder = {
                            Text(text = "Enter a message")
                        }
                    )
                    IconButton(
                        onClick = {
                            // Send the message when the icon is clicked
                            if (text.value.isNotEmpty()) {
                                messageViewModel.sendMessage(text.value.trim())
                                text.value = ""
                            }
                            messageViewModel.loadMessages()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send",tint = colorResource(R.color.custom_purple))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message: Message) {

    var isLiked by remember { mutableStateOf(false) }
    var isLikeEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val userRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(8.dp)
        ,
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700) else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (isLikeEnabled) {
                                isLiked = true // Set liked if enabled
                                isLikeEnabled = false // Disable further likes

                                val senderId = message.senderId
                                if (senderId != null) {
                                    coroutineScope.launch {
                                        when (val result = userRepository.getUserWithEmailId(senderId)) {
                                            is Result.Success -> {
                                                val user = result.data
                                                // Update user's likes count
                                                val updatedLikesCount = user.likes + 1
                                                when (val updateResult = userRepository.updateUserLikesWithEmail(user.email, updatedLikesCount)) {
                                                    is Result.Success -> {
                                                        // Disable further likes
                                                        isLikeEnabled = false
                                                        // Set liked
                                                        isLiked = true
                                                    }
                                                    is Result.Error ->{}
                                                    is Error -> {
                                                        // Handle error
                                                    }
                                                }
                                            }
                                            is Result.Error -> {
                                                // Handle error
                                                Log.e("ChatMessageItem", "Error fetching user data: ${result.exception}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            if(message.isRemoved) {
                Text(
                    text = "This message has been removed due to inappropriate content",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp)
                )
            }else{
                Text(
                    text = message.text,
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
        if (!message.isSentByCurrentUser && isLiked) {
            Box(modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700) else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp))
            {
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.ThumbUp),
                    contentDescription = "Liked",
                    tint = colorResource(id = R.color.like_yellow),
                    modifier = Modifier
                        .size(12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.senderFirstName,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
        Text(
            text = formatTimestamp(message.timestamp),
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Long): String {
    val messageDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when {
        isSameDay(messageDateTime, now) -> "today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1), now) -> "yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return formatter.format(dateTime)
}