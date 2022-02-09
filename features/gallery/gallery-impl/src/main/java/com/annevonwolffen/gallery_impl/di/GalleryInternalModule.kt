package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.remote.firebase.FirebaseImageRepository
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.ImagesInteractorImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
object GalleryInternalModule {
    @ExperimentalCoroutinesApi
    @PerFeature
    @Provides
    fun providePhotosInteractor(coroutineDispatchers: CoroutineDispatchers): ImagesInteractor {
        val databaseReference = Firebase.database.reference
            .child("dailyphotosdiary")
            .child(Firebase.auth.currentUser?.uid.orEmpty())

        val storageReference = Firebase.storage.reference
            .child("dailyphotosdiary")
            .child(Firebase.auth.currentUser?.uid.orEmpty())

        return ImagesInteractorImpl(FirebaseImageRepository(coroutineDispatchers, databaseReference, storageReference))
    }
}