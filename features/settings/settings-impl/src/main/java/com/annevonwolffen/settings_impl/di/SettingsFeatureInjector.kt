package com.annevonwolffen.settings_impl.di

import android.content.Context
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi

class SettingsFeatureInjector(featuresContainer: FeaturesContainer, private val appContext: Context) :
    BaseFeatureInjector<SettingsInternalApi>(featuresContainer) {
    override fun buildFeature(): SettingsInternalApi {
        return DaggerSettingsComponent.factory().create(appContext, getDependency(PreferencesApi::class.java))
    }
}