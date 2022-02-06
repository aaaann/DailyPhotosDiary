package com.annevonwolffen.gallery_impl.data.remote

import com.annevonwolffen.gallery_impl.domain.Photo

class ImageServerModel(
    val folder: String,
    val originalName: String,
    val name: String,
    val createdAt: String,
    val url: String
)

fun ImageServerModel.toDomain() = Photo("", name, "", createdAt, url)