package com.annevonwolffen.gallery_impl.data.remote

open class BaseImage4IOResponse(
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)