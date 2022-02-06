package com.annevonwolffen.gallery_impl.domain

data class Photo(
    val id: String,
    val name: String,
    val description: String? = null,
    val createdAt: String,
    val url: String
)
