package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_impl.data.remote.firebase.ImageEntry

data class Image(
    val id: String,
    val name: String,
    val description: String? = null,
    val createdAt: String,
    val url: String
)

fun ImageEntry.toImage(key: String?): Image = Image(
    key.orEmpty(),
    name.orEmpty(),
    description.orEmpty(),
    createdAt.orEmpty(),
    url.orEmpty()
)
