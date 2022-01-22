package com.annevonwolffen.authorization_impl

import com.annevonwolffen.authorization_api.AuthorizationScreenRouter
import com.annevonwolffen.di.PerFeature
import dagger.Binds
import dagger.Module

@Module
internal interface AuthorizationModule {

    @PerFeature
    @Binds
    fun bindAuthorizationRouter(authorizationScreenRouterImpl: AuthorizationScreenRouterImpl): AuthorizationScreenRouter
}