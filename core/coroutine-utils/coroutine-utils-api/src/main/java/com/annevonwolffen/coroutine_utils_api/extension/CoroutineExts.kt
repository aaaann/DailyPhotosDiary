package com.annevonwolffen.coroutine_utils_api.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> CoroutineScope.launchFlowCollection(flow: Flow<T>, action: (T) -> Unit) {
    launch {
        flow.collect { value -> action.invoke(value) }
    }
}