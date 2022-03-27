package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.preferences_api.PreferencesApi

class GalleryFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<GalleryExternalApi>(featuresContainer) {
    override fun buildFeature(): GalleryExternalApi {
        return DaggerGalleryComponent.factory()
            .create(
                getDependency(CoroutineUtilsApi::class),
                getDependency(PreferencesApi::class)
            )
    }
}