package com.annevonwolffen.dailyphotosdiary.di

import android.app.Application
import android.content.Context
import com.annevonwolffen.authorization_impl.AuthorizationFeatureInjectorModule
import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.FeatureInjector
import dagger.Binds
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@Module(includes = [AuthorizationFeatureInjectorModule::class])
interface FeatureInjectorsModule {

    @Singleton
    @Binds
    fun bindApplicationContext(application: Application): Context

    @Multibinds
    fun provideFeatureInjectors(): Map<Class<out Dependency>, FeatureInjector<Dependency>>
}