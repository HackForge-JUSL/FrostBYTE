package com.example.bloomer.ViewModels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.bloomer.data.Result
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.content.Context
import com.example.bloomer.data.Injection
import com.example.bloomer.data.UserRepository

class AuthViewModel() : ViewModel() {

    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, firstName, lastName)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authResult.value = userRepository.logout()

        }
    }
}