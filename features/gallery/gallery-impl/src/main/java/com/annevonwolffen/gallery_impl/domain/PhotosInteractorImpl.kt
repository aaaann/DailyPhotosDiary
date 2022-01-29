package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import javax.inject.Inject

class PhotosInteractorImpl @Inject constructor(private val photosRepository: PhotosRepository) : PhotosInteractor {
    override suspend fun loadPhotos(folder: String): Result<List<Photo>> {
        return photosRepository.loadPhotos(folder)
    }
}