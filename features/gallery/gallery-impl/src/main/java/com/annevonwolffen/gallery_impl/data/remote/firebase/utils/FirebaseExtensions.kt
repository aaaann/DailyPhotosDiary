package com.annevonwolffen.gallery_impl.data.remote.firebase.utils

import com.annevonwolffen.gallery_impl.presentation.Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun DatabaseReference.observeValue(): Flow<Result<DataSnapshot>> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Error("Ошибка при получении данных из Firebase Database: ${error.message}"))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(Result.Success(snapshot))
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }
