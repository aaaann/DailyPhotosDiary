package com.annevonwolffen.dailyphotosdiary.di

import com.annevonwolffen.authorization_impl.AuthorizationFeatureInjectorModule
import com.annevonwolffen.coroutine_utils_impl.di.CoroutineUtilsFeatureInjectorModule
import com.annevonwolffen.gallery_impl.di.GalleryFeatureInjectorModule
import com.annevonwolffen.mainscreen_impl.di.MainScreenFeatureInjectorModule
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
        MainScreenFeatureInjectorModule::class,
        NetworkFeatureInjectorModule::class,
        PreferencesFeatureInjectorModule::class,
        SettingsFeatureInjectorModule::class,
        UiUtilsFeatureInjectorModule::class]
)
interface FeatureInjectorsModule