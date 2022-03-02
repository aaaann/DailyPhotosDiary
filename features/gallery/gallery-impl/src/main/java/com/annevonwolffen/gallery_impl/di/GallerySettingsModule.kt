package com.annevonwolffen.gallery_impl.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.local.GallerySettingsRepositoryImpl
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractorImpl
import dagger.Module
import dagger.Provides

@Module
object GallerySettingsModule {

    @PerFeature
    @Provides
    fun provideSettingsInteractor(dataStore: DataStore<Preferences>): GallerySettingsInteractor {
        val settingsRepository = GallerySettingsRepositoryImpl(dataStore)
        return GallerySettingsInteractorImpl(settingsRepository)
    }
}