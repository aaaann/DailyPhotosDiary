package com.annevonwolffen.authorization_impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.authorization_api.domain.AuthInteractor
import com.annevonwolffen.authorization_api.domain.AuthState
import com.annevonwolffen.authorization_api.domain.Loading
import com.annevonwolffen.authorization_api.domain.NotSignedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthorizationViewModel(private val authInteractor: AuthInteractor) : ViewModel() {

    val authFlow: StateFlow<AuthState> get() = _authFlow
    private val _authFlow: MutableStateFlow<AuthState> = MutableStateFlow(authInteractor.isSignedIn())

    val authorizationFailure get() = _authorizationFailureEvent.receiveAsFlow()
    private val _authorizationFailureEvent = Channel<String>(Channel.CONFLATED)

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authFlow.value = Loading
            val authState = authInteractor.signUp(email, password)
            _authFlow.value = authState
            if (authState is NotSignedIn) {
                _authorizationFailureEvent.send("Ошибка при создании пользователя.")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authFlow.value = Loading
            val authState = authInteractor.signIn(email, password)
            _authFlow.value = authState
            if (authState is NotSignedIn) {
                _authorizationFailureEvent.send("Ошибка при авторизации пользователя.")
            }
        }
    }
}