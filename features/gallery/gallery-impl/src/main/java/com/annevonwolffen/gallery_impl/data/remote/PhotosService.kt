package com.annevonwolffen.gallery_impl.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosService {

    @GET("/{version}/listfolder")
    suspend fun listFolder(
        @Path("version") version: String,
        @Query("path") folder: String
    ): Response<ListFolderResponse?>
}