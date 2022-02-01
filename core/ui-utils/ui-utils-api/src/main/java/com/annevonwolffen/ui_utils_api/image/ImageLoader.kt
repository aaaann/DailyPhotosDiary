package com.annevonwolffen.ui_utils_api.image

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

interface ImageLoader {
    fun loadImage(view: ImageView, url: String, @DrawableRes placeHolder: Int? = null)
    fun loadImage(view: ImageView, uri: Uri?)
    fun loadImage(view: ImageView, file: File)
}