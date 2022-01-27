package com.annevonwolffen.gallery_impl.domain

import javax.inject.Inject

class PhotosInteractorImpl @Inject constructor() : PhotosInteractor {
    override suspend fun loadPhotos(): List<Photo> {
        return listOf(
            Photo(
                1L,
                "photo_2021-06-12 17.12.50.jpeg",
                "моя фотка",
                "2022-01-22T19:20:10.25",
                "https://cdn.image4.io/annevonwolffen/testfolder/photo_2021-06-1217.12.50.jpeg"
            ),
            Photo(
                2L,
                "Снимок экрана 2021-12-01 в 01.55.40.png",
                "Снимок экрана 2021-12-01 в 01.55.40",
                "2022-01-22T19:29:03.507",
                "https://cdn.image4.io/annevonwolffen/testfolder/c9295dd8-d80d-475a-b223-161408a96ba8.png"
            )
        )
    }
}