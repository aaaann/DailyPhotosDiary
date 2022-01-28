package com.annevonwolffen.ui_utils_api

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.ui_utils_api.image.ImageLoader

interface UiUtilsApi : Dependency {
    val imageLoader: ImageLoader
}