package com.annevonwolffen.gallery_impl.domain.settings

import kotlinx.coroutines.flow.Flow

interface GallerySettingsRepository {
    val sortOrderFlow: Flow<SortOrder>
    suspend fun toggleSortOrder()
}