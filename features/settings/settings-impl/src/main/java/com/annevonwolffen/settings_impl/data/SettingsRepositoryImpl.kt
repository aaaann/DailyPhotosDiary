package com.annevonwolffen.settings_impl.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.annevonwolffen.settings_impl.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    notificationKeyName: String
) : SettingsRepository {

    private val NOTIFICATION_KEY = booleanPreferencesKey(notificationKeyName)

    override val isNotificationEnabledFlow: Flow<Boolean>
        get() = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Ошибка при чтении preferences: $exception")
                } else {
                    Log.e(TAG, "$exception")
                }
                emit(emptyPreferences())
            }
            .map { preferences -> preferences[NOTIFICATION_KEY] ?: false }

    override suspend fun toggleNotification() {
        try {
            dataStore.edit { preferences ->
                val currentValue: Boolean = preferences[NOTIFICATION_KEY] ?: false
                preferences[NOTIFICATION_KEY] = currentValue.not()
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
        const val TAG = "SettingsRepository"
    }
}