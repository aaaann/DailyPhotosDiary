package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.AuthorizationApi
import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object AuthorizationFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(AuthorizationApi::class)
    fun provideAuthorizationFeatureInjector(featuresContainer: FeaturesContainer): FeatureInjector<out Dependency> =
        AuthorizationFeatureInjector(featuresContainer)
}