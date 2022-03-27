package com.annevonwolffen.gallery_impl.data.remote.firebase.utils

import android.util.Log
import com.annevonwolffen.gallery_impl.data.remote.firebase.FirebaseRemoteDataSource.Companion.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun DatabaseReference.observeValue(): Flow<DataSnapshot?> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Ошибка при получении данных из Firebase Database: ${error.message}")
                trySend(null)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }
