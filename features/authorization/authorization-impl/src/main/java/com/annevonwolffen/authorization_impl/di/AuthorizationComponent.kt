package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.di.PerFeature
import dagger.Component

@PerFeature
@Component(modules = [AuthorizationModule::class])
interface AuthorizationComponent : AuthorizationApi {

    @Component.Factory
    interface Factory {
        fun create(): AuthorizationComponent
    }
}