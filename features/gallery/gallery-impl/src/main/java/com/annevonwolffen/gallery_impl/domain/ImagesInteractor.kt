package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow

interface ImagesInteractor {
    fun loadImages(folder: String): Flow<Result<List<Image>>>
    suspend fun uploadImageToDatabase(folder: String, image: Image): Result<String>
    suspend fun uploadFileToStorage(folder: String, image: Image)
    suspend fun deleteImage(folder: String, image: Image): Result<Unit>
    suspend fun deleteFileFromStorage(folder: String, image: Image)
}