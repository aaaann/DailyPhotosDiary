package com.annevonwolffen.gallery_impl.data

import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.gallery_impl.data.remote.BaseImage4IOResponse
import com.annevonwolffen.gallery_impl.data.remote.PhotosService
import com.annevonwolffen.gallery_impl.data.remote.toDomain
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosRepository
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class PhotosRepositoryImpl(
    private val photosService: PhotosService,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val apiVersion: String
) : PhotosRepository {
    override suspend fun loadPhotos(folder: String): Result<List<Photo>> {
        return withContext(coroutineDispatchers.ioDispatcher) {
            processRequest(
                request = { photosService.listFolder(apiVersion, folder) },
                onSuccess = { it.images.map { image -> image.toDomain() } }
            )
        }
    }

    override suspend fun uploadImages(folder: String, uploadImage: UploadImage): Result<List<Photo>> {
        val useFileNameRqBody = "false".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val overwriteRqBody = "false".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val folderRqBody = folder.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileRqBody = uploadImage.file.run { asRequestBody("multipart/form-data".toMediaTypeOrNull()) }


        return withContext(coroutineDispatchers.ioDispatcher) {
            processRequest(
                request = {
                    photosService.uploadImage(
                        apiVersion,
                        useFileNameRqBody,
                        overwriteRqBody,
                        folderRqBody,
                        MultipartBody.Part.createFormData("file", uploadImage.file.name, fileRqBody)
                    )
                },
                onSuccess = { it.uploadedFiles.map { image -> image.toDomain() } }
            )
        }
    }

    private suspend fun <T : BaseImage4IOResponse, R> processRequest(
        request: suspend () -> Response<T?>,
        onSuccess: (T) -> R
    ): Result<R> {
        return try {
            val response = request.invoke()
            if (response.isSuccessful) {
                val result = Result.Success(response.body())
                result.value?.let { value ->
                    if (value.success && value.errors.isEmpty()) {
                        Result.Success(onSuccess.invoke(value))
                    } else {
                        Result.Error(value.errors.joinToString { ", " })
                    }
                } ?: Result.Error("Empty response body")
            } else {
                throw IOException("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            if (e is SocketTimeoutException) {
                Result.Error("Не удалось получить ответ от сервера")
            } else {
                throw e
            }
        }
    }
}