package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.local.GallerySettingsRepositoryImpl
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractorImpl
import com.annevonwolffen.preferences_api.PreferencesManager
import dagger.Module
import dagger.Provides

@Module
object GallerySettingsModule {

    @PerFeature
    @Provides
    fun provideSettingsInteractor(preferencesManager: PreferencesManager): GallerySettingsInteractor {
        val settingsRepository = GallerySettingsRepositoryImpl(preferencesManager)
        return GallerySettingsInteractorImpl(settingsRepository)
    }
}