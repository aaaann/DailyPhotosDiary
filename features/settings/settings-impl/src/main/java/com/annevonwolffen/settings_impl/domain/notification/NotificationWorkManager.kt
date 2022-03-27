package com.annevonwolffen.settings_impl.domain.notification

internal interface NotificationWorkManager {
    fun scheduleNotification()
    fun cancelNotification()
}