package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi

class AuthorizationFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<AuthorizationApi>(featuresContainer) {
    override fun buildFeature(): AuthorizationApi {
        return DaggerAuthorizationComponent.factory().create(getDependency(PreferencesApi::class))
    }
}