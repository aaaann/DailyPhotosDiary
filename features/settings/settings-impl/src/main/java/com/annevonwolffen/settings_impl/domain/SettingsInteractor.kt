package com.annevonwolffen.settings_impl.domain

import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {
    val isNotificationEnabledFlow: Flow<Boolean>
    suspend fun toggleNotification()
}