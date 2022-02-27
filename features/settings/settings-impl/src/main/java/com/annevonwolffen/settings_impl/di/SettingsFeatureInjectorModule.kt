package com.annevonwolffen.settings_impl.di

import android.content.Context
import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object SettingsFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(SettingsInternalApi::class)
    fun provideSettingsFeatureInjector(
        featuresContainer: FeaturesContainer,
        appContext: Context
    ): FeatureInjector<out Dependency> {
        return SettingsFeatureInjector(featuresContainer, appContext)
    }
}