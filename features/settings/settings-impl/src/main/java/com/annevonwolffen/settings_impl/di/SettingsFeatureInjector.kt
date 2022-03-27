package com.annevonwolffen.settings_impl.di

import android.content.Context
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi
import com.annevonwolffen.settings_api.SettingsApi

class SettingsFeatureInjector(featuresContainer: FeaturesContainer, private val appContext: Context) :
    BaseFeatureInjector<SettingsApi>(featuresContainer) {
    override fun buildFeature(): SettingsApi {
        return DaggerSettingsComponent.factory().create(appContext, getDependency(PreferencesApi::class))
    }
}