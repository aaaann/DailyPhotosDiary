package com.annevonwolffen.settings_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import com.annevonwolffen.settings_impl.domain.notification.NotificationWorkManager

interface SettingsInternalApi : Dependency {
    val settingsInteractor: SettingsInteractor
    val notificationWorkManager: NotificationWorkManager
}