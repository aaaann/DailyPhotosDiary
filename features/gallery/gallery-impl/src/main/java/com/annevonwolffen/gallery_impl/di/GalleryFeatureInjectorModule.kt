package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object GalleryFeatureInjectorModule {

    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(GalleryInternalApi::class)
    fun provideGalleryFeatureInjector(featuresContainer: FeaturesContainer): FeatureInjector<out Dependency> {
        return GalleryFeatureInjector(featuresContainer)
    }
}