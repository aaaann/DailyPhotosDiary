package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.presentation.ImagesAggregator

internal interface GalleryInternalApi : GalleryExternalApi {
    val imagesInteractor: ImagesInteractor

    val imagesAggregator: ImagesAggregator

    val settingsInteractor: GallerySettingsInteractor
}