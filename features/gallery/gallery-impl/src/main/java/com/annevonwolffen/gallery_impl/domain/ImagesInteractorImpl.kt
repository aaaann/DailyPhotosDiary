package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagesInteractorImpl @Inject constructor(private val imagesRepository: ImagesRepository) : ImagesInteractor {
    override fun loadImages(folder: String): Flow<Result<List<Image>>> {
        return imagesRepository.loadImages(folder)
    }

    override suspend fun uploadImageToDatabase(folder: String, image: Image): Result<String> {
        return imagesRepository.uploadImageToDatabase(folder, image)
    }

    override suspend fun uploadFileToStorage(folder: String, image: Image) {
        return imagesRepository.uploadFileToStorage(folder, image)
    }

    override suspend fun deleteImage(folder: String, image: Image): Result<Unit> {
        return imagesRepository.deleteImageFromDatabase(folder, image)
    }

    override suspend fun deleteFileFromStorage(folder: String, image: Image) {
        return imagesRepository.deleteFileFromStorage(folder, image)
    }
}