package com.annevonwolffen.mainscreen_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.mainscreen_api.MainScreenApi
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object MainScreenFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(MainScreenApi::class)
    fun provideAuthorizationFeatureInjector(featuresContainer: FeaturesContainer): FeatureInjector<out Dependency> =
        MainScreenFeatureInjector(featuresContainer)
}