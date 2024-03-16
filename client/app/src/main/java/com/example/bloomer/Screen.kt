package com.example.bloomer

sealed class Screen(val route:String) {
    object ProfileScreen:Screen("profilescreen")
    object JournalScreen:Screen("journalscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object ChatScreen:Screen("chatscreen")

    object DoctorScreen : Screen("doctorscreen")
    object ChatBotScreen : Screen("chatbotscreen")
    object LoginScreen:Screen("loginscreen")
    object SignupScreen:Screen("signupscreen")

    object AppointmentScreen:Screen("appointmentscreen")
}