package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.PerFeature
import dagger.Component

@PerFeature
@Component(modules = [GalleryInternalModule::class])
interface GalleryComponent : GalleryInternalApi {

    @Component.Factory
    interface Factory {
        fun create(): GalleryComponent
    }
}