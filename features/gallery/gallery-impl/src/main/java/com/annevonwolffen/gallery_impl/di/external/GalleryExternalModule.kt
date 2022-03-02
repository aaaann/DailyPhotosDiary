package com.annevonwolffen.gallery_impl.di.external

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_api.domain.ImagesExternalInteractor
import com.annevonwolffen.gallery_impl.domain.ImagesExternalInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface GalleryExternalModule {

    @PerFeature
    @Binds
    fun bindImagesExternalInteractor(impl: ImagesExternalInteractorImpl): ImagesExternalInteractor
}