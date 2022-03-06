package com.annevonwolffen.authorization_impl.domain

import com.annevonwolffen.authorization_api.domain.AuthInteractor
import com.annevonwolffen.authorization_api.domain.AuthState

class AuthInteractorImpl(private val authRepository: AuthRepository) : AuthInteractor {
    override fun isSignedIn(): AuthState {
        return authRepository.isSignedIn()
    }

    override suspend fun signIn(email: String, password: String): AuthState {
        return authRepository.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AuthState {
        return authRepository.signUp(email, password)
    }

    override fun signOut() = authRepository.signOut()
}