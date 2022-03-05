package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.AuthorizationApi
import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer

class AuthorizationFeatureInjector(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<AuthorizationApi>(featuresContainer) {
    override fun buildFeature(): AuthorizationApi {
        return DaggerAuthorizationComponent.factory().create()
    }
}