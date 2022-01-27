package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor

interface GalleryInternalApi : Dependency {
    val photosInteractor: PhotosInteractor
}