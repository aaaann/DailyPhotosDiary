package com.annevonwolffen.preferences_api

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    fun getStringPreferenceFlowByKey(key: String): Flow<String?>
    fun getBooleanPreferenceFlowByKey(key: String): Flow<Boolean?>
    suspend fun getStringPreferenceByKey(key: String): String?
    suspend fun getBooleanPreferenceByKey(key: String): Boolean?
    suspend fun editStringPreferenceByKey(key: String, edit: (String?) -> String)
    suspend fun editBooleanPreferenceByKey(key: String, edit: (Boolean?) -> Boolean)
    suspend fun clearPreferences()
}