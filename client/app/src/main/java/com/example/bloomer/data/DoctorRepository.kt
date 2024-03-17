package com.example.bloomer.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DoctorRepository(private val firestore: FirebaseFirestore) {

    suspend fun getDoctors(): Result<List<Doctors>> = try {
        val querySnapshot = firestore.collection("doctors").get().await()
        val doctors = querySnapshot.documents.map { document ->
            document.toObject(Doctors::class.java)!!.copy(id = document.id)
        }
        Result.Success(doctors)
    } catch (e: Exception) {
        Result.Error(e)
    }
}