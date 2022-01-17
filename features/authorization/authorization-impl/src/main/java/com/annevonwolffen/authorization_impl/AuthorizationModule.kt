package com.annevonwolffen.authorization_impl

import com.annevonwolffen.authorization_api.AuthorizationScreenRouter
import dagger.Binds
import dagger.Module

@Module
internal interface AuthorizationModule {

    @Binds
    fun bindAuthorizationRouter(authorizationScreenRouterImpl: AuthorizationScreenRouterImpl): AuthorizationScreenRouter
}