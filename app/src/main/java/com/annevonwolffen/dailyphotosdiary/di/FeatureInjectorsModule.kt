package com.annevonwolffen.dailyphotosdiary.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@Module(includes = [])
interface FeatureInjectorsModule {

    @Singleton
    @Binds
    fun bindApplicationContext(application: Application): Context

    @Multibinds
    fun provideFeatureInjectors(): Map<Class<out Dependency>, FeatureInjector<Dependency>>
}