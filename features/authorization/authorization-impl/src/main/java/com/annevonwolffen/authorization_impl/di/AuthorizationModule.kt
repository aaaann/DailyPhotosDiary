package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.domain.AuthInteractor
import com.annevonwolffen.authorization_impl.data.AuthRepositoryImpl
import com.annevonwolffen.authorization_impl.domain.AuthInteractorImpl
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
object AuthorizationModule {

    @PerFeature
    @Provides
    fun provideAuthInteractor(preferencesManager: PreferencesManager): AuthInteractor {
        val authRepository = AuthRepositoryImpl(Firebase.auth)
        return AuthInteractorImpl(authRepository, preferencesManager)
    }
}