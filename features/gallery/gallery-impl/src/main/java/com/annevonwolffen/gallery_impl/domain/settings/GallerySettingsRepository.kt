package com.annevonwolffen.gallery_impl.domain.settings

import kotlinx.coroutines.flow.Flow

interface GallerySettingsRepository {
    val sortOrderFlow: Flow<SortOrder>
    suspend fun getInitialSortOrder(): SortOrder
    suspend fun toggleSortOrder()
}