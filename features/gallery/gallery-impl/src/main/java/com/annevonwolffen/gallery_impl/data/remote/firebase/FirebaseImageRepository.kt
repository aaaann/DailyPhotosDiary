package com.annevonwolffen.gallery_impl.data.remote.firebase

import android.util.Log
import androidx.core.net.toUri
import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.gallery_impl.data.remote.firebase.utils.observeValue
import com.annevonwolffen.gallery_impl.domain.ImagesRepository
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.domain.toImage
import com.annevonwolffen.gallery_impl.presentation.AddImageFragment
import com.annevonwolffen.gallery_impl.presentation.Result
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class FirebaseImageRepository(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val rootDatabaseReference: DatabaseReference,
    private val rootStorageReference: StorageReference
) : ImagesRepository {
    override fun loadPhotos(folder: String): Flow<Result<List<Image>>> {
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

    override suspend fun uploadImages(folder: String, imageEntry: ImageEntry): Result<List<Image>> {
        return withContext(coroutineDispatchers.ioDispatcher) {
            val dbReference = rootDatabaseReference.child(folder)
            val storageReference = rootStorageReference
                .child(folder)
                .child(imageEntry.name.orEmpty())
            val generatedId = dbReference.push().key.orEmpty()

            val dbDeferredResult = async { dbReference.child(generatedId).setValue(imageEntry).await() }
            launch {
                val uploadTask = storageReference.putFile(imageEntry.url.orEmpty().toUri()).await()
                if (uploadTask.task.isSuccessful) {
                    runCatching {
                        val url = storageReference.downloadUrl.await()
                        // TODO: нужно ли try{}catch{} или если ошибка, то она сверху кинется и заканселит эту корутину тоже
                        dbDeferredResult.await().also {
                            // обновить ссылку на изображение в базе
                            dbReference
                                .child(generatedId)
                                .setValue(imageEntry.copy(id = generatedId, url = url.toString()))
                        }
                    }.onFailure { t -> Log.d(TAG, "Ошибка при получении ссылки на картинку: ${t.message}") }
                } else {
                    uploadTask.task.exception?.let {
                        Log.d(TAG, "Ошибка при загрузке изображения в Firebase Storage: ${it.message}")
                    }
                }
            }

            // TODO: get result from dbDeferredResult

            return@withContext Result.Success(emptyList())
        }
    }

    private companion object {
        const val TAG = "FirebaseImageRepository"
    }
}
