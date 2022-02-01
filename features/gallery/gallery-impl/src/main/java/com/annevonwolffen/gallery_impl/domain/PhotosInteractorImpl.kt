package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import javax.inject.Inject

class PhotosInteractorImpl @Inject constructor(private val photosRepository: PhotosRepository) : PhotosInteractor {
    override suspend fun loadPhotos(folder: String): Result<List<Photo>> {
        return photosRepository.loadPhotos(folder)
    }

    override suspend fun uploadImages(folder: String, uploadImage: UploadImage): Result<List<Photo>> {
        return photosRepository.uploadImages(folder, uploadImage)
    }
}