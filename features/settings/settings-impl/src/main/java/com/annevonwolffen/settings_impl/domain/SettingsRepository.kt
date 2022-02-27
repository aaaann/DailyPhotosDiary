package com.annevonwolffen.settings_impl.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isNotificationEnabledFlow: Flow<Boolean>
    suspend fun toggleNotification()
}