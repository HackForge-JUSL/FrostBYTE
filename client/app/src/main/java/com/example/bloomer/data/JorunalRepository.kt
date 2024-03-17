package com.example.bloomer.data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class JournalRepository(private val firestore: FirebaseFirestore) : ViewModel() {

    suspend fun addJournal(journal: Journal, userId: String): Result<Unit> = try {
        val documentReference = firestore.collection("users").document(userId)
            .collection("journals").add(journal).await()
        val journalId = documentReference.id
        val journalWithId = journal.copy(id = journalId)
        firestore.collection("users").document(userId)
            .collection("journals").document(journalId)
            .set(journalWithId).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun updateJournal(journalId: String, updatedJournal: Journal, userId: String): Result<Unit> = try {
        firestore.collection("users").document(userId)
            .collection("journals").document(journalId)
            .set(updatedJournal).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }


    fun getUserJournals(userId: String): Flow<List<Journal>> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .collection("journals")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    val journals = it.documents.map { doc ->
                        val journal = doc.toObject(Journal::class.java)!!
                        journal.copy(id = doc.id) // Set the id field with the document ID
                    }
                    trySend(journals).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }

}