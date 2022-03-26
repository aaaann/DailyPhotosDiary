package com.annevonwolffen.settings_impl.data

import com.annevonwolffen.preferences_api.PreferencesManager
import com.annevonwolffen.settings_impl.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val preferencesManager: PreferencesManager,
    private val notificationKeyName: String
) : SettingsRepository {

    override val isNotificationEnabledFlow: Flow<Boolean>
        get() = preferencesManager.getBooleanPreferenceFlowByKey(notificationKeyName)
            .map { it ?: false }

    override suspend fun toggleNotification() {
        preferencesManager.editBooleanPreferenceByKey(notificationKeyName) { (it ?: false).not() }
    }
}