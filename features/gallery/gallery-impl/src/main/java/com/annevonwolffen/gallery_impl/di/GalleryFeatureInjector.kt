package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer

class GalleryFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<GalleryInternalApi>(featuresContainer) {
    override fun buildFeature(): GalleryInternalApi {
        return DaggerGalleryComponent.factory().create()
    }
}