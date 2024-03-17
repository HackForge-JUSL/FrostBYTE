package com.example.bloomer.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloomer.data.DoctorRepository
import com.example.bloomer.data.Doctors
import com.example.bloomer.data.Injection
import com.example.bloomer.data.Result
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {
    private val _doctors = MutableLiveData<List<Doctors>>()
    val doctors: LiveData<List<Doctors>> = _doctors
    private val doctorRepository: DoctorRepository = DoctorRepository(Injection.instance())

    init {
        loadDoctors()
    }

    fun loadDoctors() {
        viewModelScope.launch {
            when (val result = doctorRepository.getDoctors()) {
                is Result.Success -> _doctors.value = result.data ?: emptyList()
                is Result.Error -> {

                }
            }
        }
    }
}