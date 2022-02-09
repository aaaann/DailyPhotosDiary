package com.annevonwolffen.gallery_impl.data.remote.firebase

import com.annevonwolffen.gallery_impl.domain.Image
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ImageEntry(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createdAt: String? = null,
    val url: String? = null
)

fun ImageEntry.toImage(key: String?): Image = Image(
    key.orEmpty(),
    name.orEmpty(),
    description.orEmpty(),
    createdAt.orEmpty(),
    url.orEmpty()
)

fun ImageEntry.fromImage(image: Image): ImageEntry = this.copy(
    id = image.id,
    name = image.name,
    description = image.description,
    createdAt = image.createdAt, // TODO: maybe different types and parsing needed
    url = image.url
)