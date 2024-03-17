package com.example.bloomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.bloomer.ui.theme.BloomerTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bloomer.ViewModels.AuthViewModel
import com.example.bloomer.screens.AppointmentScreen
import com.example.bloomer.screens.ChatBotScreen
import com.example.bloomer.screens.ChatRoomListScreen
import com.example.bloomer.screens.ChatScreen
import com.example.bloomer.screens.DoctorScreen
import com.example.bloomer.screens.JournalScreen
import com.example.bloomer.screens.LoginScreen
import com.example.bloomer.screens.ProfileScreen
import com.example.bloomer.screens.SignUpScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            BloomerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var startDestination = Screen.JournalScreen.route
    val context = LocalContext.current
    val user = Firebase.auth.currentUser
    if (user != null) {
        // User is signed in
    } else {
        // No user is signed in
        startDestination = Screen.SignupScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }

        composable(Screen.JournalScreen.route){
            JournalScreen(navController = navController,authViewModel = authViewModel,)
        }

        composable(Screen.ChatBotScreen.route) {

        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = { navController.navigate(Screen.JournalScreen.route) }
            )
        }

        composable(Screen.DoctorScreen.route) {

        }

        composable("${Screen.AppointmentScreen.route}/{doctorName}") {

        }

        composable(Screen.ChatRoomsScreen.route) {

        }

        composable(Screen.ProfileScreen.route) {

        }

        composable("${Screen.ChatScreen.route}/{roomId}") {

        }
    }
}