package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.domain.PhotosInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface GalleryInternalModule {

    @PerFeature
    @Binds
    fun bindPhotosInteractor(interactorImpl: PhotosInteractorImpl): PhotosInteractor
}