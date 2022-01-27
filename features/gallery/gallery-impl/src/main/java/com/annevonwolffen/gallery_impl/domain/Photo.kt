package com.annevonwolffen.gallery_impl.domain

data class Photo(
    val id: Long,
    val name: String,
    val description: String? = null,
    val createdAt: String,
    val url: String
)
