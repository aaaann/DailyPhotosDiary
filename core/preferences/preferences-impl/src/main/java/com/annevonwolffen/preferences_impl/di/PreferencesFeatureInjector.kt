package com.annevonwolffen.preferences_impl.di

import android.content.Context
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi

class PreferencesFeatureInjector(private val appContext: Context, featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<PreferencesApi>(featuresContainer) {
    override fun buildFeature(): PreferencesApi {
        return DaggerPreferencesComponent.factory().create(appContext)
    }
}