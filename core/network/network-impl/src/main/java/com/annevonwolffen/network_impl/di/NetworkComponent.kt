package com.annevonwolffen.network_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.network_api.NetworkApi
import dagger.Component

@PerFeature
@Component(modules = [NetworkModule::class])
interface NetworkComponent : NetworkApi {
    @Component.Factory
    interface Factory {
        fun create(): NetworkComponent
    }
}