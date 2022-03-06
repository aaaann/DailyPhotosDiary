package com.annevonwolffen.authorization_api.di

import com.annevonwolffen.authorization_api.domain.AuthInteractor
import com.annevonwolffen.di.Dependency

interface AuthorizationApi : Dependency {
    val authInteractor: AuthInteractor
}