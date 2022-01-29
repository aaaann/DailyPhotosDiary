package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_impl.data.PhotosRepositoryImpl
import com.annevonwolffen.gallery_impl.data.remote.PhotosService
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.domain.PhotosInteractorImpl
import com.annevonwolffen.coroutine_utils_api.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
object GalleryInternalModule {
    val IMAGE4IO_URL = "https://api.image4.io/"
    val TOKEN = "NUYvWkg2eVU5NDlNRmhiTmpLQ0ZiZz09Oi9LODYvMk0xSGpCS2dDMnhGcS9kTG9adEZNcnZFeWEwOVdKM1pWNUE5Ulk9"
    val IMAGE4IO_VERSION = "v1.0"

    @PerFeature
    @Provides
    fun providePhotosInteractor(
        okHttpClient: OkHttpClient,
        retrofitClient: Retrofit,
        coroutineDispatchers: CoroutineDispatchers
    ): PhotosInteractor {
        val newOkHttpClient = okHttpClient.newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Basic $TOKEN")
                    .build()
                chain.proceed(request)
            }.build()

        val newRetrofitClient = retrofitClient.newBuilder()
            .baseUrl(IMAGE4IO_URL)
            .client(newOkHttpClient)
            .build()

        val photosService = newRetrofitClient.create(PhotosService::class.java)
        return PhotosInteractorImpl(PhotosRepositoryImpl(photosService, coroutineDispatchers, IMAGE4IO_VERSION))
    }
}