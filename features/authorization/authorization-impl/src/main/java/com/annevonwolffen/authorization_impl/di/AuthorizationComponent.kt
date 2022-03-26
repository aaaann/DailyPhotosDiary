package com.annevonwolffen.authorization_impl.di

import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesApi
import dagger.Component

@PerFeature
@Component(modules = [AuthorizationModule::class], dependencies = [PreferencesApi::class])
interface AuthorizationComponent : AuthorizationApi {

    @Component.Factory
    interface Factory {
        fun create(preferencesApi: PreferencesApi): AuthorizationComponent
    }
}