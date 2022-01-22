package com.annevonwolffen.mainscreen_impl.di

import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.mainscreen_api.MainScreenApi

internal class MainScreenFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<MainScreenApi>(featuresContainer) {
    override fun buildFeature(): MainScreenApi {
        return DaggerMainScreenComponent.factory().create()
    }
}