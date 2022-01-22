package com.annevonwolffen.authorization_api

import com.annevonwolffen.di.Dependency

interface AuthorizationApi : Dependency {
    val authorizationRouter: AuthorizationScreenRouter
}