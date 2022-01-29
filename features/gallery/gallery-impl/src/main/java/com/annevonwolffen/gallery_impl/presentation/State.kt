package com.annevonwolffen.gallery_impl.presentation

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<T>(val value: T) : State<T>()
    data class Error(val errorMessage: String? = null) : State<Nothing>()
}