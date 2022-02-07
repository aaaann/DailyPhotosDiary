package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow

interface ImagesInteractor {
    fun loadPhotos(folder: String): Flow<Result<List<Image>>>
    suspend fun uploadImages(folder: String, uploadImage: UploadImage): Result<List<Image>>
}