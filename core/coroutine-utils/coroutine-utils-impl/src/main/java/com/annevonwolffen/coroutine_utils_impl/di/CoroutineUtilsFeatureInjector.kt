package com.annevonwolffen.coroutine_utils_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import javax.inject.Inject

class CoroutineUtilsFeatureInjector @Inject constructor(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<CoroutineUtilsApi>(featuresContainer) {
    override fun buildFeature(): CoroutineUtilsApi {
        return DaggerCoroutineUtilsComponent.factory().create()
    }
}