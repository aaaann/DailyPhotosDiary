package com.annevonwolffen.gallery_impl.data.remote

import com.annevonwolffen.gallery_impl.domain.Image

class UploadedImageServerModel(
    val name: String,
    val url: String,
    val createdAtUTC: String
)

fun UploadedImageServerModel.toDomain() = Image("", name, "", createdAtUTC, url)