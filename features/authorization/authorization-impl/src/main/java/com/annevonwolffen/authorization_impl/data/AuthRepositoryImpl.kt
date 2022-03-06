package com.annevonwolffen.authorization_impl.data

import android.util.Log
import com.annevonwolffen.authorization_api.domain.AuthState
import com.annevonwolffen.authorization_api.domain.NotSignedIn
import com.annevonwolffen.authorization_api.domain.SignedIn
import com.annevonwolffen.authorization_impl.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override fun isSignedIn(): AuthState {
        return firebaseAuth.currentUser?.let {
            Log.d(TAG, "Signed in, email - ${it.email.toString()}, uid - ${it.uid}")
            SignedIn
        } ?: NotSignedIn
    }

    override suspend fun signIn(email: String, password: String): AuthState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await().let {
                it.user?.let { user ->
                    Log.d(TAG, "Signed in, email - ${user.email.toString()}, uid - ${user.uid}")
                    SignedIn
                } ?: NotSignedIn
            }
        } catch (e: FirebaseAuthException) {
            Log.d(TAG, e.toString())
            NotSignedIn
        }
    }

    override suspend fun signUp(email: String, password: String): AuthState {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await().let {
                it.user?.let { user ->
                    Log.d(TAG, "Signed up, email - ${user.email.toString()}, uid - ${user.uid}")
                    SignedIn
                } ?: NotSignedIn
            }
        } catch (e: FirebaseAuthException) {
            Log.d(TAG, e.toString())
            NotSignedIn
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private companion object {
        const val TAG = "AuthRepository"
    }
}