package com.annevonwolffen.ui_utils_impl.image

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.bumptech.glide.Glide
import java.io.File
import javax.inject.Inject

class GlideImageLoader @Inject constructor() : ImageLoader {
    override fun loadImage(view: ImageView, url: String, @DrawableRes placeHolder: Int?) {
        Glide.with(view)
            .load(url)
            .fitCenter()
            .run {
                placeHolder?.let { this.placeholder(it) }
                this
            }
            .run {
                placeHolder?.let { this.error(it) }
                this
            }
            .centerCrop()
            .into(view)
    }

    override fun loadImage(view: ImageView, uri: Uri?) {
        Glide.with(view)
            .load(uri)
            .centerCrop()
            .into(view)
    }

    override fun loadImage(view: ImageView, file: File) {
        Glide.with(view)
            .load(file)
            .centerCrop()
            .into(view)
    }
}