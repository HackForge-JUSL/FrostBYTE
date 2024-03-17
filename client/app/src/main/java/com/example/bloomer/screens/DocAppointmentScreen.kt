package com.example.bloomer.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.R
import com.example.bloomer.data.Doctors
import com.example.bloomer.data.items1

@Composable
fun AppointmentScreen(doctorName : String,navController: NavController, context: Context,){

    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(3)
    }

    var patient_name by remember { mutableStateOf("") }
    var patient_phone_no by remember { mutableStateOf("") }
    var app_date by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppBarView(
                fontSize = 20.sp,
                stringResourceId = R.string.appointment_screen_app_bar,
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

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)
                    .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally )
                {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(colorResource(id = R.color.custom_grey)),
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(text = doctorName,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center
                            ),modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = patient_name,
                        onValueChange = { patient_name = it },
                        label = { Text(text = "Patient Name")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = patient_phone_no,
                        onValueChange = { patient_phone_no = it },
                        label = { Text(text = "Mobile Number")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = app_date,
                        onValueChange = { app_date = it },
                        label = { Text(text = "Appointment Date")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        if(patient_name.isNotBlank() && patient_phone_no.isNotBlank() && app_date.isNotBlank()) {
                            Toast.makeText(
                                context,
                                "Appointment Booked. We will contact you soon",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigateUp()
                        }else{
                            Toast.makeText(context, "Please fill all the details", Toast.LENGTH_LONG).show()
                        }

                    }) {
                        Text(text = "Book Appointment")
                    }
                }

            }
        }
    }
}