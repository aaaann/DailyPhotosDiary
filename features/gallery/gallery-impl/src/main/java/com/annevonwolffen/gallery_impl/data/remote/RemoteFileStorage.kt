package com.annevonwolffen.gallery_impl.data.remote

import com.annevonwolffen.gallery_impl.data.ImageDto

internal interface RemoteFileStorage {
    suspend fun uploadFileToStorage(folder: String, image: ImageDto): String
    suspend fun deleteFileFromStorage(folder: String, image: ImageDto)
}