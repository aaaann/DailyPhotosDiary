package com.annevonwolffen.gallery_impl.di.external

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.gallery_api.di.GalleryExternalApi

class GalleryExternalFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<GalleryExternalApi>(featuresContainer) {
    override fun buildFeature(): GalleryExternalApi {
        return DaggerGalleryExternalComponent.factory()
            .create(getDependency(CoroutineUtilsApi::class.java))
    }
}