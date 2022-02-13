package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor

interface GalleryInternalApi : Dependency {
    val imagesInteractor: ImagesInteractor
}