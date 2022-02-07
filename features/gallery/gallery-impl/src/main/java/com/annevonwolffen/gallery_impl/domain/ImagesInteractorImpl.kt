package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagesInteractorImpl @Inject constructor(private val imagesRepository: ImagesRepository) : ImagesInteractor {
    override fun loadPhotos(folder: String): Flow<Result<List<Image>>> {
        return imagesRepository.loadPhotos(folder)
    }

    override suspend fun uploadImages(folder: String, uploadImage: UploadImage): Result<List<Image>> {
        return imagesRepository.uploadImages(folder, uploadImage)
    }
}