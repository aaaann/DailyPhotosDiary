package com.annevonwolffen.gallery_impl.data

import com.annevonwolffen.gallery_impl.data.remote.ListFolderResponse
import com.annevonwolffen.gallery_impl.data.remote.PhotosService
import com.annevonwolffen.gallery_impl.data.remote.toDomain
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosRepository
import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.withContext
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
            val result: Result<ListFolderResponse?> = processRequest { photosService.listFolder(apiVersion, folder) }
            when (result) {
                is Result.Success -> result.value?.let { listfolder ->
                    if (listfolder.success && listfolder.errors.isEmpty()) {
                        Result.Success(listfolder.images.map { it.toDomain() })
                    } else {
                        Result.Error(listfolder.errors.joinToString { ", " })
                    }
                } ?: Result.Error("Empty response body")
                is Result.Error -> result
            }
        }
    }

    private suspend fun <T> processRequest(
        request: suspend () -> Response<T>
    ): Result<T?> {
        return try {
            val response = request.invoke()
            if (response.isSuccessful) {
                Result.Success(response.body())
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