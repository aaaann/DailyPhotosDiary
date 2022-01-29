package com.annevonwolffen.coroutine_utils_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.coroutine_utils_impl.CoroutineDispatchersImpl
import com.annevonwolffen.di.PerFeature
import dagger.Binds
import dagger.Module

@Module
interface CoroutineUtilsModule {

    @PerFeature
    @Binds
    fun bindCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}