package com.annevonwolffen.gallery_impl.data.remote.firebase

import android.util.Log
import androidx.core.net.toUri
import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.gallery_impl.data.remote.firebase.utils.observeValue
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.ImagesRepository
import com.annevonwolffen.gallery_impl.presentation.Result
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class FirebaseImageRepository(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val rootDatabaseReference: DatabaseReference,
    private val rootStorageReference: StorageReference
) : ImagesRepository {
    override fun loadImages(folder: String): Flow<Result<List<Image>>> {
        return rootDatabaseReference
            .child(folder)
            .observeValue()
            .map {
                when (it) {
                    is Result.Success -> {
                        val images: List<Image> = it.value.children
                            .mapNotNull { snapshot ->
                                val key = snapshot.key
                                snapshot?.getValue(ImageEntry::class.java)?.run { toImage(key) }
                            }
                        Result.Success(images)
                    }
                    is Result.Error -> {
                        it
                    }
                }
            }
            .flowOn(coroutineDispatchers.ioDispatcher)
    }

    override suspend fun getImages(folder: String): Result<List<Image>> {
        val dbReference = rootDatabaseReference.child(folder)
        return withContext(coroutineDispatchers.ioDispatcher) {
            try {
                dbReference.get().await().children
                    .mapNotNull { snapshot ->
                        val key = snapshot.key
                        snapshot?.getValue(ImageEntry::class.java)?.run { toImage(key) }
                    }.let { Result.Success(it) }
            } catch (e: Exception) {
                Log.d(TAG, "Ошибка при получении данных из Firebase Database: ${e.message}")
                Result.Error(e.message)
            }
        }
    }

    override suspend fun uploadImageToDatabase(folder: String, image: Image): Result<String> {
        val dbReference = rootDatabaseReference.child(folder)
        val generatedId = image.id ?: dbReference.push().key.orEmpty()

        val imageEntry = ImageEntry().fromImage(image).copy(id = generatedId)

        return withContext(coroutineDispatchers.ioDispatcher) {
            try {
                dbReference.child(generatedId).setValue(imageEntry).await()
                Log.d(TAG, "Объект $imageEntry успешно загружен в Realtime Database, id: $generatedId")
                Result.Success(generatedId)
            } catch (e: Exception) {
                Log.d(TAG, "Ошибка при загрузке объекта $imageEntry в Realtime Database, $e")
                Result.Error(e.message)
            }
        }
    }

    override suspend fun uploadFileToStorage(folder: String, image: Image) {
        val storageReference = rootStorageReference
            .child(folder)
            .child(image.name)

        val imageEntry = ImageEntry().fromImage(image)

        return withContext(coroutineDispatchers.ioDispatcher) {
            val uploadTask = storageReference.putFile(imageEntry.url.orEmpty().toUri()).await()
            if (uploadTask.task.isSuccessful) {
                runCatching {
                    val url = storageReference.downloadUrl.await()
                    // обновить ссылку на изображение в базе
                    uploadImageToDatabase(folder, image.copy(url = url.toString()))
                }.onFailure { t -> Log.d(TAG, "Ошибка при получении ссылки на картинку: ${t.message}") }
            } else {
                uploadTask.task.exception?.let {
                    Log.d(TAG, "Ошибка при загрузке изображения в Firebase Storage: ${it.message}")
                }
            }
        }
    }

    override suspend fun deleteImageFromDatabase(folder: String, image: Image): Result<Unit> {
        val dbReference = rootDatabaseReference.child(folder).child(image.id.orEmpty())
        return withContext(coroutineDispatchers.ioDispatcher) {
            try {
                dbReference.removeValue().await()
                Log.d(TAG, "Объект $image успешно удален из Realtime Database")
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.d(TAG, "Ошибка при удалении объекта ${image.id} из Realtime Database, $e")
                Result.Error(e.message)
            }
        }
    }

    override suspend fun deleteFileFromStorage(folder: String, image: Image) {
        val storageReference = rootStorageReference
            .child(folder)
            .child(image.name)
        return withContext(coroutineDispatchers.ioDispatcher) {
            runCatching {
                storageReference.delete().await()
            }.onFailure { t -> Log.d(TAG, "Ошибка при удалении картинки: ${t.message}") }
        }
    }

    private companion object {
        const val TAG = "FirebaseImageRepository"
    }
}
