package com.annevonwolffen.gallery_impl.di

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.network_api.NetworkApi
import dagger.Component

@PerFeature
@Component(modules = [GalleryInternalModule::class], dependencies = [CoroutineUtilsApi::class, NetworkApi::class])
interface GalleryComponent : GalleryInternalApi {

    @Component.Factory
    interface Factory {
        fun create(
            coroutineUtilsApi: CoroutineUtilsApi,
            networkApi: NetworkApi
        ): GalleryComponent
    }
}