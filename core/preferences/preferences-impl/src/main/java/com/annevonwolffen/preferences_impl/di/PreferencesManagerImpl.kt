package com.annevonwolffen.preferences_impl.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.annevonwolffen.preferences_api.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesManagerImpl(private val dataStore: DataStore<Preferences>) : PreferencesManager {

    override fun getStringPreferenceFlowByKey(key: String): Flow<String?> {
        return getPreferenceFlowByKey(stringPreferencesKey(key))
    }

    override fun getBooleanPreferenceFlowByKey(key: String): Flow<Boolean?> {
        return getPreferenceFlowByKey(booleanPreferencesKey(key))
    }

    private fun <T> getPreferenceFlowByKey(preferencesKey: Preferences.Key<T>): Flow<T?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Error reading preferences by key [${preferencesKey.name}]: $exception")
                } else {
                    Log.e(TAG, "$exception")
                }
                emit(emptyPreferences())
            }
            .map { preferences -> preferences[preferencesKey] }
    }

    override suspend fun getStringPreferenceByKey(key: String): String? {
        return getPreferenceByKey(stringPreferencesKey(key))
    }

    override suspend fun getBooleanPreferenceByKey(key: String): Boolean? {
        return getPreferenceByKey(booleanPreferencesKey(key))
    }

    private suspend fun <T> getPreferenceByKey(preferencesKey: Preferences.Key<T>): T? {
        return dataStore.data.first().toPreferences()[preferencesKey]
    }

    override suspend fun editStringPreferenceByKey(key: String, edit: (String?) -> String) {
        editPreferenceByKey(stringPreferencesKey(key), edit)
    }

    override suspend fun editBooleanPreferenceByKey(key: String, edit: (Boolean?) -> Boolean) {
        editPreferenceByKey(booleanPreferencesKey(key), edit)
    }

    private suspend fun <T> editPreferenceByKey(preferencesKey: Preferences.Key<T>, edit: (T?) -> T) {
        try {
            dataStore.edit { preferences ->
                preferences[preferencesKey] = edit(preferences[preferencesKey])
            }
        } catch (exception: Exception) {
            if (exception is IOException) {
                Log.e(TAG, "Error writing preference by key [${preferencesKey.name}]: $exception")
            } else {
                Log.e(TAG, "$exception")
            }
        }
    }

    private companion object {
        const val TAG = "PreferencesInteractor"
    }
}