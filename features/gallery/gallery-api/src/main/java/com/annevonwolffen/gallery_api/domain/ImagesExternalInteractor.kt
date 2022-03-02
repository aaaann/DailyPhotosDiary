package com.annevonwolffen.gallery_api.domain

interface ImagesExternalInteractor {

    suspend fun hasImagesForToday(folder: String): Boolean
}