package com.example.bloomer.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.R
import com.example.bloomer.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bloomer.ViewModels.AuthViewModel
import com.example.bloomer.ViewModels.JournalViewModel
import com.example.bloomer.data.Journal
import com.example.bloomer.data.items
import com.example.bloomer.data.items1
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(navController: NavController,
                  authViewModel: AuthViewModel,
                  viewModel : JournalViewModel = viewModel()){

    val journals by viewModel.journals.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    viewModel.loadJournals()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            if (item.title == "Logout") {
                                authViewModel.logout()
                            }
                            navController.navigate(item.route)
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            androidx.compose.material3.Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },

                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {

        Scaffold(
            topBar = {
                TopAppBar(modifier = Modifier
                    .height(90.dp),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
//                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.journal_screen_app_bar),
                                color = colorResource(id = R.color.white),
                                modifier = Modifier
//                            .padding(start = 2.dp)
                                    .heightIn(max = 48.dp)
//                                    .align(Alignment.Center)
//
                                ,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp
                                ),
                            )
                        }
                    },
                    elevation = 10.dp,
                    backgroundColor = colorResource(id = R.color.custom_purple),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Menu,
                                tint = Color.White,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
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
                                androidx.compose.material3.Icon(
                                    imageVector = if (index == selectedItemIndex1) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(all = 20.dp),
                    contentColor = Color.White,
                    backgroundColor = Color.Black,
                    onClick = {
//                    Toast.makeText(context, "FAButton Clicked", Toast.LENGTH_LONG).show()
                        showDialog = true

                    }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            },
        )

        {
            Box(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.custom_grey))
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 25.dp)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(journals) { journal ->
                            JournalItem(journal)
                        }
                    }

                    if (showDialog) {
                        AlertDialog(onDismissRequest = { showDialog = true },
                            title = {
                                Row {
                                    Text("Create a new journal",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(top = 8.dp,start = 6.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = { showDialog = false }) {
                                        Icon(painter = rememberVectorPainter(Icons.Default.Close), contentDescription = "close dialog")
                                    }
                                }
                            },
                            text = {
                                Column {
                                    OutlinedTextField(
                                        value = title,
                                        onValueChange = { title = it },
                                        singleLine = true,
                                        label = { Text(text = "Title")},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                    )
                                    OutlinedTextField(
                                        value = text,
                                        onValueChange = { text = it },
                                        singleLine = false,
                                        label = { Text(text = "Content")},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .height(90.dp)
                                    )
                                }

                            }, confirmButton = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = {
                                            if (text.isNotBlank()) {
                                                showDialog = false
                                                viewModel.addJournal(title, text)
                                                title = ""
                                                text = ""
                                            }
                                        }
                                    ) {
                                        Text("Add")
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(width = 400.dp, height = 400.dp)
                                .background(Color.White)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalItem(journal : Journal){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = formatTimestamp(journal.timestamp).uppercase(Locale.getDefault()), fontSize = 16.sp, fontWeight = FontWeight.Normal,textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = journal.title,
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = journal.text, fontSize = 16.sp, fontWeight = FontWeight.Normal,)

        }
    }


}