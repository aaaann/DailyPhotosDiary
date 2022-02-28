package com.annevonwolffen.settings_impl.domain.notification

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationWorkManagerImpl(private val workManager: WorkManager) : NotificationWorkManager {
    override fun scheduleNotification() {
        workManager
            .enqueueUniquePeriodicWork(
                NOTIFICATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<DailyNotificationWorker>(15, TimeUnit.MINUTES)
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build() // TODO: configure work request (maybe OneTimeWorkRequest)
            )
    }

    override fun cancelNotification() {
        workManager.cancelUniqueWork(NOTIFICATION_WORK_NAME)
    }

    private companion object {
        const val NOTIFICATION_WORK_NAME = "DailyNotificationWork"
    }
}