package com.annevonwolffen.gallery_impl.presentation

sealed class Result<out T : Any?> {
    data class Success<T : Any?>(val value: T) : Result<T>()
    data class Error(val errorMessage: String? = null) : Result<Nothing>()
} // TODO: move to some core module