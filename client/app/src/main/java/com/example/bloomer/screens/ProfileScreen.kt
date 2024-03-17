package com.example.bloomer.screens

import android.widget.Space
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bloomer.AppBarView
import com.example.bloomer.R
import com.example.bloomer.Screen
import com.example.bloomer.ViewModels.AuthViewModel
import com.example.bloomer.data.Injection
import com.example.bloomer.data.UserRepository
import com.example.bloomer.data.items1
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(authViewModel: AuthViewModel,navController: NavController){

    var user_firstname by remember { mutableStateOf("") }
    var user_lastname by remember { mutableStateOf("") }
    var user_email by remember { mutableStateOf("") }
    var user_likes by remember { mutableStateOf(0) }

    val userRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    LaunchedEffect(key1 = true) {
        val result = userRepository.getCurrentUser()
        when (result) {
            is com.example.bloomer.data.Result.Success -> {
               user_firstname = result.data.firstName
                user_lastname = result.data.lastName
                user_email = result.data.email
                user_likes = result.data.likes
            }
            is com.example.bloomer.data.Result.Error -> {
                navController.navigate(Screen.SignupScreen.route)
            }

        }

    }

    Scaffold(
        topBar = {
            AppBarView(
                fontSize = 25.sp,
                stringResourceId = R.string.profile_screen_app_bar,
            ) {navController.navigateUp() }
        },
    )
    {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(colorResource(id = R.color.custom_grey))) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(modifier = Modifier.wrapContentSize()
                    .height(64.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically

                )  {
                    Text(text = "BLOOMER ACCOUNT",style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    ),modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()
                    .height(64.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Name : $user_firstname $user_lastname",style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),modifier = Modifier.padding(horizontal = 16.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()
                    .height(64.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Email : $user_email",style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),modifier = Modifier.padding(horizontal = 16.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()
                    .height(64.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Coins accumulated : $user_likes",style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),modifier = Modifier.padding(horizontal = 16.dp))
                }

            }
        }
    }
}