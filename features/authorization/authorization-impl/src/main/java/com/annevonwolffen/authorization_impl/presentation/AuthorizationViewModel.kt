package com.annevonwolffen.authorization_impl.presentation

import androidx.lifecycle.ViewModel
import com.annevonwolffen.authorization_impl.domain.AuthInteractor
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthorizationViewModel(private val authInteractor: AuthInteractor) : ViewModel() {

    val userFlow: StateFlow<FirebaseUser?> get() = _userFlow
    private val _userFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
}