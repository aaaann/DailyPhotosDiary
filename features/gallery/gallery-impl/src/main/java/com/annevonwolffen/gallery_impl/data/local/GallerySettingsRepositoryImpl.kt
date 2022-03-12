package com.annevonwolffen.gallery_impl.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsRepository
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.domain.settings.getOpposite
import com.annevonwolffen.gallery_impl.domain.settings.sortOrderByName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class GallerySettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : GallerySettingsRepository {
    override val sortOrderFlow: Flow<SortOrder>
        get() = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Ошибка при чтении preferences: $exception")
                } else {
                    Log.e(TAG, "$exception")
                }
                emit(emptyPreferences())
            }
            .map { preferences -> preferences[SORT_ORDER_KEY].sortOrderByName() }

    override suspend fun getInitialSortOrder(): SortOrder {
        return dataStore.data.first().toPreferences()[SORT_ORDER_KEY].sortOrderByName()
    }

    override suspend fun toggleSortOrder() {
        try {
            dataStore.edit { preferences ->
                val curSortOrder: SortOrder = preferences[SORT_ORDER_KEY].sortOrderByName()
                preferences[SORT_ORDER_KEY] = curSortOrder.getOpposite().name
            }
        } catch (exception: Exception) {
            if (exception is IOException) {
                Log.e(TAG, "Ошибка при записи preferences на диск: $exception")
            } else {
                Log.e(TAG, "$exception")
            }
        }
    }

    private companion object {
        const val TAG = "GallerySettingsRepository"
        const val SORT_ORDER_KEY_NAME = "sort_order"
        val SORT_ORDER_KEY = stringPreferencesKey(SORT_ORDER_KEY_NAME)
    }
}