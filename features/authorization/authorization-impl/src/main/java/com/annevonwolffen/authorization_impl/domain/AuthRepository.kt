package com.annevonwolffen.authorization_impl.domain

import com.annevonwolffen.authorization_api.domain.AuthState

interface AuthRepository {
    fun isSignedIn(): AuthState
    suspend fun signIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AuthState
    fun signOut()
    fun getUserEmail(): String
}