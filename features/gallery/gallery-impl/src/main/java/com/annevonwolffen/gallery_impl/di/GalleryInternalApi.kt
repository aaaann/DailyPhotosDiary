package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.presentation.ImagesAggregator

interface GalleryInternalApi : Dependency {
    val imagesInteractor: ImagesInteractor

    val imagesAggregator: ImagesAggregator

    val settingsInteractor: GallerySettingsInteractor
}