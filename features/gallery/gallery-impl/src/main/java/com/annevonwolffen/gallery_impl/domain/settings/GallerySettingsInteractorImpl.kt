package com.annevonwolffen.gallery_impl.domain.settings

import kotlinx.coroutines.flow.Flow

class GallerySettingsInteractorImpl(private val settingsRepository: GallerySettingsRepository) :
    GallerySettingsInteractor {
    override val sortOrderFlow: Flow<SortOrder> = settingsRepository.sortOrderFlow

    override suspend fun toggleSortOrder() = settingsRepository.toggleSortOrder()
}