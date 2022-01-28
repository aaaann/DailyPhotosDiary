package com.annevonwolffen.ui_utils_impl.image.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.di.DependencyKey
import com.annevonwolffen.di.FeatureInjector
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface UiUtilsFeatureInjectorModule {

    @Singleton
    @Binds
    @IntoMap
    @DependencyKey(UiUtilsApi::class)
    fun bindFeatureInjector(impl: UiUtilsFeatureInjector): FeatureInjector<out Dependency>
}