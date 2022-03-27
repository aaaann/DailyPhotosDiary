package com.annevonwolffen.gallery_impl.domain.settings

import kotlinx.coroutines.flow.Flow

internal interface GallerySettingsInteractor {
    val sortOrderFlow: Flow<SortOrder>
    suspend fun getInitialSortOrder(): SortOrder
    suspend fun toggleSortOrder()
}