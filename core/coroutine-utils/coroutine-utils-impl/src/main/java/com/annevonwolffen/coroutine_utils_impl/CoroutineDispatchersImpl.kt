package com.annevonwolffen.coroutine_utils_impl

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatchersImpl @Inject constructor() : CoroutineDispatchers {
    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    override val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
}