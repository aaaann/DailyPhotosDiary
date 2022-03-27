package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.ImagesRepositoryImpl
import com.annevonwolffen.gallery_impl.data.remote.firebase.FirebaseRemoteDataSource
import com.annevonwolffen.gallery_impl.data.remote.firebase.FirebaseRemoteFileStorage
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.ImagesInteractorImpl
import com.annevonwolffen.gallery_impl.domain.ImagesRepository
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
        @Provides
        fun provideImagesRepository(coroutineDispatchers: CoroutineDispatchers): ImagesRepository {
            val databaseReference = Firebase.database.reference
                .child("dailyphotosdiary")
                .child(Firebase.auth.currentUser?.uid.orEmpty())

            val remoteDataSource = FirebaseRemoteDataSource(databaseReference)

            val storageReference = Firebase.storage.reference
                .child("dailyphotosdiary")
                .child(Firebase.auth.currentUser?.uid.orEmpty())

            val remoteFileStorage = FirebaseRemoteFileStorage(storageReference)

            return ImagesRepositoryImpl(coroutineDispatchers, remoteDataSource, remoteFileStorage)
        }

        @ExperimentalCoroutinesApi
        @Provides
        fun provideImagesInteractor(imagesRepository: ImagesRepository): ImagesInteractor {
            return ImagesInteractorImpl(imagesRepository)
        }
    }
}