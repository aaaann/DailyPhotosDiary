package com.annevonwolffen.gallery_impl.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val date: Long,
    val url: String
) : Parcelable