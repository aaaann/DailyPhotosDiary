package com.annevonwolffen.gallery_impl.data.remote

import com.annevonwolffen.gallery_impl.data.ImageDto
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {
    fun getImagesFlow(folder: String): Flow<List<ImageDto>?>
    suspend fun getImages(folder: String): List<ImageDto>
    suspend fun uploadImage(folder: String, image: ImageDto): String
    suspend fun deleteImage(folder: String, image: ImageDto)
}