package com.annevonwolffen.gallery_impl.di.external

import com.annevonwolffen.coroutine_utils_api.CoroutineUtilsApi
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.gallery_impl.di.GalleryInternalModule
import dagger.Component

@PerFeature
@Component(
    modules = [GalleryInternalModule::class, GalleryExternalModule::class],
    dependencies = [CoroutineUtilsApi::class]
)
interface GalleryExternalComponent : GalleryExternalApi {

    @Component.Factory
    interface Factory {
        fun create(coroutineUtilsApi: CoroutineUtilsApi): GalleryExternalComponent
    }
}