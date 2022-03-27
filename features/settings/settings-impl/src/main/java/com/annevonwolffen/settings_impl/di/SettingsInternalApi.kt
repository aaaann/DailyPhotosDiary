package com.annevonwolffen.settings_impl.di

import com.annevonwolffen.settings_api.SettingsApi
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import com.annevonwolffen.settings_impl.domain.notification.NotificationWorkManager

internal interface SettingsInternalApi : SettingsApi {
    val settingsInteractor: SettingsInteractor
    val notificationWorkManager: NotificationWorkManager
}