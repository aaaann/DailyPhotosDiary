package com.annevonwolffen.mainscreen_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.mainscreen_api.MainScreenApi
import dagger.Component

@PerFeature
@Component(modules = [MainScreenModule::class])
interface MainScreenComponent : MainScreenApi{

    @Component.Factory
    interface Factory {
        fun create(): MainScreenComponent
    }
}