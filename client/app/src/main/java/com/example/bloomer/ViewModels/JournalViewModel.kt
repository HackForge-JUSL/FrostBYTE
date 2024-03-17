package com.example.bloomer.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloomer.data.Injection
import com.example.bloomer.data.Journal
import com.example.bloomer.data.JournalRepository
import com.example.bloomer.data.Result
import com.example.bloomer.data.User
import com.example.bloomer.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class JournalViewModel : ViewModel() {

    private val journalRepository: JournalRepository
    private val userRepository: UserRepository

    init {
        journalRepository = JournalRepository(Injection.instance())
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        loadCurrentUser()
    }

    private val _journals = MutableLiveData<List<Journal>>()
    val journals: LiveData<List<Journal>> get() = _journals

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser


    fun addJournal(title: String, text: String, journalId: String = "") {
        if (_currentUser.value != null) {
            val journal = journalId.let {
                Journal(
                    id = it,
                    title = title,
                    text = text
                )
            }
            viewModelScope.launch {
                when (val result = userRepository.getCurrentUser()) {
                    is Result.Success ->
                        when (val addResult =
                            journalRepository.addJournal( journal, result.data.email)) {
                            is Result.Success -> {
                                loadJournals()
                            }
                            is Result.Error -> {
                                // Handle error
                            }
                        }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }

    fun updateJournal(journalId: String, updatedTitle: String, updatedText: String) {
        if (_currentUser.value != null) {
            viewModelScope.launch {
                when (val result = userRepository.getCurrentUser()) {
                    is Result.Success -> {
                        when (val updateResult = journalRepository.updateJournal(journalId, Journal(title = updatedTitle, text = updatedText), result.data.email)) {
                            is Result.Success -> {
                                loadJournals() // Reload journals after updating
                            }
                            is Result.Error -> {
                                // Handle error
                            }
                        }
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            }
        }
    }

    fun loadJournals() {
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()) {
                is Result.Success ->
                    journalRepository.getUserJournals(result.data.email)
                        .collect { _journals.value = it }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data?:null
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }
}

