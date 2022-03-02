package com.annevonwolffen.gallery_impl.di.external

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object GalleryExternalFeatureInjectorModule {
    @Singleton
    @Provides
    @IntoMap
    @DependencyKey(GalleryExternalApi::class)
    fun provideGalleryExternalFeatureInjector(featuresContainer: FeaturesContainer): FeatureInjector<out Dependency> {
        return GalleryExternalFeatureInjector(featuresContainer)
    }
}