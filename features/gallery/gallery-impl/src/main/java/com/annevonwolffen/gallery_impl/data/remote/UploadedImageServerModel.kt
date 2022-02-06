package com.annevonwolffen.gallery_impl.data.remote

import com.annevonwolffen.gallery_impl.domain.Photo

class UploadedImageServerModel(
    val name: String,
    val url: String,
    val createdAtUTC: String
)

fun UploadedImageServerModel.toDomain() = Photo("", name, "", createdAtUTC, url)