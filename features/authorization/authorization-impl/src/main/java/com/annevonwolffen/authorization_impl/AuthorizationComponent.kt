package com.annevonwolffen.authorization_impl

import com.annevonwolffen.authorization_api.AuthorizationApi
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