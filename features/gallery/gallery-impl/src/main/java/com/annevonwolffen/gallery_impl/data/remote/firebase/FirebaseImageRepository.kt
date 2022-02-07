package com.annevonwolffen.gallery_impl.data.remote.firebase

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.gallery_impl.data.remote.firebase.utils.observeValue
import com.annevonwolffen.gallery_impl.domain.ImagesRepository
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.domain.toImage
import com.annevonwolffen.gallery_impl.presentation.Result
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class FirebaseImageRepository(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val rootDatabaseReference: DatabaseReference
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

    override suspend fun uploadImages(folder: String, uploadImage: UploadImage): Result<List<Image>> {
        TODO("Not yet implemented")
    }
}