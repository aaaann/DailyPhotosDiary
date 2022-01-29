package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result

interface PhotosInteractor {

    suspend fun loadPhotos(folder: String): Result<List<Photo>>
}