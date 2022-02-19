package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun loadImages(folder: String): Flow<Result<List<Image>>>
    suspend fun uploadImageToDatabase(folder: String, image: Image): Result<String>
    suspend fun uploadFileToStorage(folder: String, image: Image)
    suspend fun deleteImageFromDatabase(folder: String, image: Image): Result<Unit>
    suspend fun deleteFileFromStorage(folder: String, image: Image)
}