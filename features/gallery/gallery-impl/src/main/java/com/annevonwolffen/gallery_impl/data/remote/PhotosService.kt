package com.annevonwolffen.gallery_impl.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosService {

    @GET("/{version}/listfolder")
    suspend fun listFolder(
        @Path("version") version: String,
        @Query("path") folder: String
    ): Response<ListFolderResponse?>

    @Multipart
    @POST("/{version}/uploadImage")
    suspend fun uploadImage(
        @Path("version") version: String,
        @Part("useFilename") useFilename: RequestBody,
        @Part("overwrite") overwrite: RequestBody,
        @Part("path") folder: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<UploadImageResponse?>
}