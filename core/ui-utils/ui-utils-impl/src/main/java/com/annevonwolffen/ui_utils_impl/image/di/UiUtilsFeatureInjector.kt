package com.annevonwolffen.ui_utils_impl.image.di

import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import javax.inject.Inject

class UiUtilsFeatureInjector @Inject constructor(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<UiUtilsApi>(featuresContainer) {
    override fun buildFeature(): UiUtilsApi {
        return DaggerUiUtilsComponent.factory().create()
    }
}