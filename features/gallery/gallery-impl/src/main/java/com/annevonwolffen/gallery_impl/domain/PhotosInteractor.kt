package com.annevonwolffen.gallery_impl.domain

interface PhotosInteractor {

    suspend fun loadPhotos(): List<Photo>
}