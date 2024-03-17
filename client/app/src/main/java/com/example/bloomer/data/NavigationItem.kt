package com.example.bloomer.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MobileFriendly
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MobileFriendly
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Recommend
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bloomer.Screen

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val items = listOf(
    NavigationItem(
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = Screen.ProfileScreen.route
    ),
    NavigationItem(
        title = "Logout",
        selectedIcon = Icons.Filled.Logout,
        unselectedIcon = Icons.Outlined.Logout,
        route = Screen.SignupScreen.route
    ),
)

val items1 = listOf(
    NavigationItem(
        title = "Journal",
        selectedIcon = Icons.Filled.StickyNote2,
        unselectedIcon = Icons.Outlined.StickyNote2,
        route = Screen.JournalScreen.route
    ),
    NavigationItem(
        title = "ChatRooms",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.People,
        route = Screen.ChatRoomsScreen.route
    ),
    NavigationItem(
        title = "Bloom Bear",
        selectedIcon = Icons.Filled.MobileFriendly,
        unselectedIcon = Icons.Outlined.MobileFriendly,
        route = Screen.ChatBotScreen.route
    ),
    NavigationItem(
        title = "Doctor",
        selectedIcon = Icons.Filled.HealthAndSafety,
        unselectedIcon = Icons.Outlined.HealthAndSafety,
        route = Screen.DoctorScreen.route
    ),
)