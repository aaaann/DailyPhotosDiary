package com.annevonwolffen.gallery_impl.data.remote

class ListFolderResponse(
    val folders: List<FolderServerModel>,
    val images: List<ImageServerModel>,
    val continuationToken: String,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)