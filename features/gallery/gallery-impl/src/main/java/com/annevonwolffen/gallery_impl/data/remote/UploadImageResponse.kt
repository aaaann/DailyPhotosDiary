package com.annevonwolffen.gallery_impl.data.remote

class UploadImageResponse(
    val uploadedFiles: List<UploadedImageServerModel>,
    success: Boolean,
    errors: List<String>,
    messages: List<String>
) : BaseImage4IOResponse(success, errors, messages)