package com.annevonwolffen.network_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.network_api.NetworkApi
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface NetworkFeatureInjectorModule {

    @Singleton
    @Binds
    @IntoMap
    @DependencyKey(NetworkApi::class)
    fun bindFeatureInjector(impl: NetworkFeatureInjector): FeatureInjector<out Dependency>
}