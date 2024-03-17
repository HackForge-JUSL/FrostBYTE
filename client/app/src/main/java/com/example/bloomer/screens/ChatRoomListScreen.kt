package com.example.bloomer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.R
import com.example.bloomer.ViewModels.RoomViewModel
import com.example.bloomer.data.Room
import com.example.bloomer.data.items1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomListScreen(
    roomViewModel: RoomViewModel = viewModel(),
    navController: NavController,
    onJoinClicked: (Room) -> Unit,

) {
    val rooms by roomViewModel.rooms.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(1)
    }

    Scaffold(
        topBar = {
            AppBarView(
                fontSize = 30.sp,
                stringResourceId = R.string.chatroom_screen_app_bar,
            ) {navController.navigateUp() }
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
        }

        )
    {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(rooms) { room ->
                        RoomItem(room = room, onJoinClicked = { onJoinClicked(room) })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to create a new room
                Button(
                    onClick = {

                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Room")
                }


                if (showDialog) {
                    AlertDialog(onDismissRequest = { showDialog = true },
                        title = { Text("Create a new room") },
                        text = {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }, confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        if (name.isNotBlank()) {
                                            showDialog = false
                                            roomViewModel.createRoom(name)
                                            name = ""
                                        }
                                    }
                                ) {
                                    Text("Add")
                                }
                                Button(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, onJoinClicked: (Room) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.custom_grey)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = room.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp),
            textAlign = TextAlign.Center)
        OutlinedButton(
            onClick = { onJoinClicked(room) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Join",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }
    }
}
