package com.annevonwolffen.gallery_impl.data.remote.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ImageEntry(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createdAt: String? = null,
    val url: String? = null
)