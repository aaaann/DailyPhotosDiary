package com.annevonwolffen.gallery_impl.data.remote.firebase

import android.util.Log
import androidx.core.net.toUri
import com.annevonwolffen.gallery_impl.data.ImageDto
import com.annevonwolffen.gallery_impl.data.remote.RemoteFileStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

internal class FirebaseRemoteFileStorage(private val rootStorageReference: StorageReference) : RemoteFileStorage {
    override suspend fun uploadFileToStorage(folder: String, image: ImageDto): String {
        val storageReference = rootStorageReference
            .child(folder)
            .child(image.name.orEmpty())

        val uploadTask = storageReference.putFile(image.url.orEmpty().toUri()).await()
        if (uploadTask.task.isSuccessful) {
            return runCatching {
                storageReference.downloadUrl.await().toString()
            }.getOrElse { t ->
                Log.d(TAG, "Ошибка при получении ссылки на картинку: ${t.message}")
                throw t
            }
        } else {
            throw Exception(
                "Ошибка при загрузке изображения в Firebase Storage: ${uploadTask.task.exception?.message}"
            ).also { Log.d(TAG, it.message.orEmpty()) }
        }
    }

    override suspend fun deleteFileFromStorage(folder: String, image: ImageDto) {
        val storageReference = rootStorageReference
            .child(folder)
            .child(image.name.orEmpty())
        runCatching {
            storageReference.delete().await()
        }.onFailure { t -> Log.d(TAG, "Ошибка при удалении картинки: ${t.message}") }
    }

    private companion object {
        const val TAG = "FirebaseRemoteFileStorage"
    }
}