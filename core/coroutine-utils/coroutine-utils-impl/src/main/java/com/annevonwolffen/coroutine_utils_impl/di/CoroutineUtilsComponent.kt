package com.annevonwolffen.coroutine_utils_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.PerFeature
import dagger.Component

@PerFeature
@Component(modules = [CoroutineUtilsModule::class])
interface CoroutineUtilsComponent : CoroutineUtilsApi {

    @Component.Factory
    interface Factory {
        fun create(): CoroutineUtilsComponent
    }
}