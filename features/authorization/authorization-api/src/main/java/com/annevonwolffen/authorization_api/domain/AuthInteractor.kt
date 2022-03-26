package com.annevonwolffen.authorization_api.domain

interface AuthInteractor {
    fun isSignedIn(): AuthState
    suspend fun signIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AuthState
    suspend fun signOut()
}