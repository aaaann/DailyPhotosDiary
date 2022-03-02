package com.annevonwolffen.gallery_api.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.gallery_api.domain.ImagesExternalInteractor

interface GalleryExternalApi : Dependency {
    val imagesExternalInteractor: ImagesExternalInteractor
}