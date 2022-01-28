package com.annevonwolffen.ui_utils_impl.image

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.bumptech.glide.Glide
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
}