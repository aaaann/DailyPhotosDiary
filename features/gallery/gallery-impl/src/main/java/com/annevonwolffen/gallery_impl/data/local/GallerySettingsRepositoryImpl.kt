package com.annevonwolffen.gallery_impl.data.local

import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsRepository
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.domain.settings.getOpposite
import com.annevonwolffen.gallery_impl.domain.settings.sortOrderByName
import com.annevonwolffen.preferences_api.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GallerySettingsRepositoryImpl(private val preferencesManager: PreferencesManager) : GallerySettingsRepository {
    override val sortOrderFlow: Flow<SortOrder>
        get() = preferencesManager.getStringPreferenceFlowByKey(SORT_ORDER_KEY_NAME)
            .map { it.sortOrderByName() }

    override suspend fun getInitialSortOrder(): SortOrder {
        return preferencesManager.getStringPreferenceByKey(SORT_ORDER_KEY_NAME).sortOrderByName()
    }

    override suspend fun toggleSortOrder() {
        preferencesManager.editStringPreferenceByKey(SORT_ORDER_KEY_NAME) { currentSortOrder ->
            currentSortOrder.sortOrderByName().getOpposite().name
        }
    }

    private companion object {
        const val SORT_ORDER_KEY_NAME = "sort_order"
    }
}