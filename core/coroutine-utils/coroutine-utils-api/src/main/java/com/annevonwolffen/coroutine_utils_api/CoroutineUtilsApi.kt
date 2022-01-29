package com.annevonwolffen.coroutine_utils_api

import com.annevonwolffen.di.Dependency

interface CoroutineUtilsApi : Dependency {
    val coroutineDispatchers: CoroutineDispatchers
}