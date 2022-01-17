package com.annevonwolffen.authorization_impl

import com.annevonwolffen.authorization_api.AuthorizationApi
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeaturesContainer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface AuthorizationFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(AuthorizationApi::class)
    fun provideAuthorizationFeatureInjector(featuresContainer: FeaturesContainer) =
        AuthorizationFeatureInjector(featuresContainer)
}