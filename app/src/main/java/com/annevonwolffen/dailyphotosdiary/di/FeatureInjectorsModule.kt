package com.annevonwolffen.dailyphotosdiary.di

import com.annevonwolffen.authorization_impl.di.AuthorizationFeatureInjectorModule
import com.annevonwolffen.coroutine_utils_impl.di.CoroutineUtilsFeatureInjectorModule
import com.annevonwolffen.gallery_impl.di.GalleryFeatureInjectorModule
import com.annevonwolffen.network_impl.di.NetworkFeatureInjectorModule
import com.annevonwolffen.preferences_impl.di.PreferencesFeatureInjectorModule
import com.annevonwolffen.settings_impl.di.SettingsFeatureInjectorModule
import com.annevonwolffen.ui_utils_impl.image.di.UiUtilsFeatureInjectorModule
import dagger.Module

@Module(
    includes = [
        AuthorizationFeatureInjectorModule::class,
        CoroutineUtilsFeatureInjectorModule::class,
        GalleryFeatureInjectorModule::class,
        NetworkFeatureInjectorModule::class,
        PreferencesFeatureInjectorModule::class,
        SettingsFeatureInjectorModule::class,
        UiUtilsFeatureInjectorModule::class]
)
interface FeatureInjectorsModule