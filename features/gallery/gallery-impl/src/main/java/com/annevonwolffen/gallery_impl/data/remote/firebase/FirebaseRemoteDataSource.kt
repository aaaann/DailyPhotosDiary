package com.annevonwolffen.gallery_impl.data.remote.firebase

import android.util.Log
import com.annevonwolffen.gallery_impl.data.ImageDto
import com.annevonwolffen.gallery_impl.data.remote.RemoteDataSource
import com.annevonwolffen.gallery_impl.data.remote.firebase.utils.observeValue
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
internal class FirebaseRemoteDataSource(private val rootDatabaseReference: DatabaseReference) : RemoteDataSource {
    override fun getImagesFlow(folder: String): Flow<List<ImageDto>?> {
        return rootDatabaseReference
            .child(folder)
            .observeValue()
            .map {
                it?.children?.mapNotNull { snapshot ->
                    val key = snapshot.key
                    snapshot?.getValue(ImageDto::class.java)?.copy(id = key)
                }
            }
    }

    override suspend fun getImages(folder: String): List<ImageDto> {
        val dbReference = rootDatabaseReference.child(folder)
        try {
            return dbReference.get().await().children
                .mapNotNull { snapshot ->
                    val key = snapshot.key
                    snapshot?.getValue(ImageDto::class.java)?.copy(id = key)
                }
        } catch (e: Exception) {
            Log.d(TAG, "Ошибка при получении данных из Firebase Database: ${e.message}")
            throw e
        }
    }

    override suspend fun uploadImage(folder: String, image: ImageDto): String {
        val dbReference = rootDatabaseReference.child(folder)
        val generatedId = image.id ?: dbReference.push().key.orEmpty()

        val updatedImage = image.copy(id = generatedId)
        try {
            dbReference.child(generatedId).setValue(updatedImage).await()
            Log.d(TAG, "Объект $updatedImage успешно загружен в Realtime Database, id: $generatedId")
            return generatedId
        } catch (e: Exception) {
            Log.d(TAG, "Ошибка при загрузке объекта $updatedImage в Realtime Database, $e")
            throw e
        }
    }

    override suspend fun deleteImage(folder: String, image: ImageDto) {
        val dbReference = rootDatabaseReference.child(folder).child(image.id.orEmpty())
        try {
            dbReference.removeValue().await()
            Log.d(TAG, "Объект $image успешно удален из Realtime Database")
        } catch (e: Exception) {
            Log.d(TAG, "Ошибка при удалении объекта ${image.id} из Realtime Database, $e")
            throw e
        }
    }

    companion object {
        const val TAG = "FirebaseRemoteDataSource"
    }
}