package com.annevonwolffen.dailyphotosdiary.di

import android.app.Application
import android.content.Context
import com.annevonwolffen.authorization_impl.AuthorizationFeatureInjectorModule
import com.annevonwolffen.mainscreen_impl.di.MainScreenFeatureInjectorModule
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        AuthorizationFeatureInjectorModule::class,
        MainScreenFeatureInjectorModule::class]
)
interface FeatureInjectorsModule {

    @Singleton
    @Binds
    fun bindApplicationContext(application: Application): Context
}