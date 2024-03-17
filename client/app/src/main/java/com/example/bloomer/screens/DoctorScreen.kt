package com.example.bloomer.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.R
import com.example.bloomer.ViewModels.DoctorViewModel
import com.example.bloomer.data.Doctors
import com.example.bloomer.data.Room
import com.example.bloomer.data.items1

@Composable
fun DoctorScreen(
    doctorViewModel: DoctorViewModel = viewModel(),
    navController: NavController,
    onAppointmentClicked: (Doctors) -> Unit,
){
    val doctors by doctorViewModel.doctors.observeAsState(emptyList())
    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(3)
    }

    doctorViewModel.loadDoctors()

    Scaffold(
        topBar = {
            AppBarView(
                fontSize = 20.sp,
                stringResourceId = R.string.doctor_screen_app_bar,
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
                    items(doctors) { doctor ->
                        DoctorItem(doctor = doctor, navController, onAppointmentClicked = { onAppointmentClicked(doctor) })
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorItem(doctor : Doctors,navController: NavController, onAppointmentClicked: (Doctors) -> Unit,){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.custom_grey)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )

        // Spacer to separate image and text
        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), // Take remaining space
            contentAlignment = Alignment.CenterStart // Align text to start
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = doctor.name,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "address")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = doctor.address,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row {
//                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(imageVector = Icons.Filled.Phone, contentDescription = "phone")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = doctor.mobile,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Book Appointment >",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                run { onAppointmentClicked(doctor) }
                            },
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}