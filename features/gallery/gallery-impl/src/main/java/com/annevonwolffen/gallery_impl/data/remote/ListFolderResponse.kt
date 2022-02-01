package com.annevonwolffen.gallery_impl.data.remote

class ListFolderResponse(
    val folders: List<FolderServerModel>,
    val images: List<ImageServerModel>,
    val continuationToken: String,
    success: Boolean,
    errors: List<String>,
    messages: List<String>
) : BaseImage4IOResponse(success, errors, messages)