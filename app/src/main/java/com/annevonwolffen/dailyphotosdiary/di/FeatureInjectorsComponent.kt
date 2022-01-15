package com.annevonwolffen.dailyphotosdiary.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FeatureInjectorsModule::class])
interface FeatureInjectorsComponent {
    val featureInjectors: Map<Class<out Dependency>, FeatureInjector<Dependency>>

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance featuresContainer: FeaturesContainer
        ): FeatureInjectorsComponent
    }
}