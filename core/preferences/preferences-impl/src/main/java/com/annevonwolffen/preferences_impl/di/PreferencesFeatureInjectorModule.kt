package com.annevonwolffen.preferences_impl.di

import android.content.Context
import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.preferences_api.PreferencesApi
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object PreferencesFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(PreferencesApi::class)
    fun providePreferencesFeatureInjector(
        appContext: Context,
        featuresContainer: FeaturesContainer
    ): FeatureInjector<out Dependency> {
        return PreferencesFeatureInjector(appContext, featuresContainer)
    }
}