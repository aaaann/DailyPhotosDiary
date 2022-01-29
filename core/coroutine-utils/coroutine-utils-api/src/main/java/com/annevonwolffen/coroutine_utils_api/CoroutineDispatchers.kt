package com.annevonwolffen.coroutine_utils_api

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val computationDispatcher: CoroutineDispatcher
}