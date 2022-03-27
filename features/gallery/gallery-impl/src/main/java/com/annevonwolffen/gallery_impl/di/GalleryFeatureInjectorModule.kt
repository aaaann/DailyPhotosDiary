package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.gallery_impl.di.GalleryFeatureInjector
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object GalleryFeatureInjectorModule {
    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(GalleryExternalApi::class)
    fun provideGalleryExternalFeatureInjector(featuresContainer: FeaturesContainer): FeatureInjector<out Dependency> {
        return GalleryFeatureInjector(featuresContainer)
    }
}