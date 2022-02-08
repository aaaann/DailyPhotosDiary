package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.data.remote.firebase.ImageEntry
import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun loadPhotos(folder: String): Flow<Result<List<Image>>>
    suspend fun uploadImages(folder: String, imageEntry: ImageEntry): Result<List<Image>>
}