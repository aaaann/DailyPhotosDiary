package com.annevonwolffen.settings_impl.domain

import kotlinx.coroutines.flow.Flow

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override val isNotificationEnabledFlow: Flow<Boolean> = settingsRepository.isNotificationEnabledFlow

    override suspend fun toggleNotification() = settingsRepository.toggleNotification()
}