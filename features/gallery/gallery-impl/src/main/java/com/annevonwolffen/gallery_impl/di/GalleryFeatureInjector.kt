package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi

class GalleryFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<GalleryInternalApi>(featuresContainer) {
    override fun buildFeature(): GalleryInternalApi {
        return DaggerGalleryComponent.factory()
            .create(
                getDependency(CoroutineUtilsApi::class.java),
                getDependency(PreferencesApi::class.java)
            )
    }
}