package com.annevonwolffen.ui_utils_api.image

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
    fun loadImage(view: ImageView, url: String, @DrawableRes placeHolder: Int? = null)
}