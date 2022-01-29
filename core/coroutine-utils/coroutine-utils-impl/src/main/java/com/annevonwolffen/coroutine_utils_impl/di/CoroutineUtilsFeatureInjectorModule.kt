package com.annevonwolffen.coroutine_utils_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface CoroutineUtilsFeatureInjectorModule {

    @Singleton
    @Binds
    @IntoMap
    @DependencyKey(CoroutineUtilsApi::class)
    fun bindFeatureInjector(impl: CoroutineUtilsFeatureInjector): FeatureInjector<out Dependency>
}