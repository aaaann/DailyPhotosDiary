package com.annevonwolffen.ui_utils_impl.image.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.annevonwolffen.ui_utils_impl.image.GlideImageLoader
import dagger.Binds
import dagger.Module

@Module
interface UiUtilsModule {

    @PerFeature
    @Binds
    fun bindImageLoader(impl: GlideImageLoader): ImageLoader
}