package com.annevonwolffen.dailyphotosdiary.di

import android.app.Application
import android.content.Context
import com.annevonwolffen.authorization_impl.AuthorizationFeatureInjectorModule
import com.annevonwolffen.coroutine_utils_impl.di.CoroutineUtilsFeatureInjectorModule
import com.annevonwolffen.gallery_impl.di.GalleryFeatureInjectorModule
import com.annevonwolffen.mainscreen_impl.di.MainScreenFeatureInjectorModule
import com.annevonwolffen.network_impl.di.NetworkFeatureInjectorModule
import com.annevonwolffen.ui_utils_impl.image.di.UiUtilsFeatureInjectorModule
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        AuthorizationFeatureInjectorModule::class,
        CoroutineUtilsFeatureInjectorModule::class,
        GalleryFeatureInjectorModule::class,
        MainScreenFeatureInjectorModule::class,
        NetworkFeatureInjectorModule::class,
        UiUtilsFeatureInjectorModule::class]
)
interface FeatureInjectorsModule {

    @Singleton
    @Binds
    fun bindApplicationContext(application: Application): Context
}