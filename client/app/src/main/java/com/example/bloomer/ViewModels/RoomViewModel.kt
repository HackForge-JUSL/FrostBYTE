package com.example.bloomer.ViewModels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloomer.data.Injection
import com.example.bloomer.data.Result.*
import com.example.bloomer.data.Room
import com.example.bloomer.data.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: MutableLiveData<List<Room>> = _rooms
    private val roomRepository: RoomRepository = RoomRepository(Injection.instance())

    init {
        loadRooms()
    }

    fun createRoom(name: String) {
        viewModelScope.launch {
            val result = roomRepository.createRoom(name)
            // Handling the result if needed
            when (result) {
                is Success -> {
                    // Room created successfully
                    loadRooms()
                }
                is Error -> {
                    // Handle error case
                }
            }
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            when (val result = roomRepository.getRooms()) {
                is Success -> _rooms.value = result.data ?: emptyList()
                is Error -> {

                }
            }
        }
    }
}