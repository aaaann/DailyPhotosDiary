package com.annevonwolffen.ui_utils_impl.image.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import dagger.Component

@PerFeature
@Component(modules = [UiUtilsModule::class])
interface UiUtilsComponent : UiUtilsApi {

    @Component.Factory
    interface Factory {
        fun create(): UiUtilsComponent
    }
}