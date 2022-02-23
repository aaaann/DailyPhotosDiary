package com.annevonwolffen.gallery_impl.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.local.GallerySettingsRepositoryImpl
import com.annevonwolffen.gallery_impl.data.remote.firebase.FirebaseImageRepository
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.ImagesInteractorImpl
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractorImpl
import com.annevonwolffen.gallery_impl.presentation.ImagesAggregator
import com.annevonwolffen.gallery_impl.presentation.ImagesAggregatorImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
interface GalleryInternalModule {

    @PerFeature
    @Binds
    fun bindImagesAggregator(impl: ImagesAggregatorImpl): ImagesAggregator

    companion object {
        @ExperimentalCoroutinesApi
        @PerFeature
        @Provides
        fun provideImagesInteractor(coroutineDispatchers: CoroutineDispatchers): ImagesInteractor {
            val databaseReference = Firebase.database.reference
                .child("dailyphotosdiary")
                .child(Firebase.auth.currentUser?.uid.orEmpty())

            val storageReference = Firebase.storage.reference
                .child("dailyphotosdiary")
                .child(Firebase.auth.currentUser?.uid.orEmpty())

            return ImagesInteractorImpl(
                FirebaseImageRepository(
                    coroutineDispatchers,
                    databaseReference,
                    storageReference
                )
            )
        }

        @PerFeature
        @Provides
        fun provideSettingsInteractor(dataStore: DataStore<Preferences>): GallerySettingsInteractor {
            val settingsRepository = GallerySettingsRepositoryImpl(dataStore)
            return GallerySettingsInteractorImpl(settingsRepository)
        }
    }
}