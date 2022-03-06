package com.annevonwolffen.authorization_api.domain

sealed class AuthState
object SignedIn : AuthState()
object NotSignedIn : AuthState()
object Loading: AuthState()
