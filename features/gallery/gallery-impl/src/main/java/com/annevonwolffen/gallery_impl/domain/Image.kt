package com.annevonwolffen.gallery_impl.domain

data class Image(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val createdAt: Long,
    val url: String
)