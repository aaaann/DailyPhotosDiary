package com.annevonwolffen.settings_impl.domain.notification

import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorkManagerImpl(private val workManager: WorkManager) : NotificationWorkManager {
    override fun scheduleNotification() {
        workManager
            .enqueueUniquePeriodicWork(
                NOTIFICATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<DailyNotificationWorker>(REPEAT_INTERVAL_HOURS, TimeUnit.HOURS)
                    .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                    .build()
            )
    }

    private fun calculateInitialDelay(): Long {
        val notificationCalendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val currentCalendar: Calendar = Calendar.getInstance()
        val delay = notificationCalendar.timeInMillis - currentCalendar.timeInMillis
        Log.d("NotificationWorkManager", "init delay: $delay")
        return delay.takeIf { it > 0 } ?: 0
    }

    override fun cancelNotification() {
        workManager.cancelUniqueWork(NOTIFICATION_WORK_NAME)
    }

    private companion object {
        const val NOTIFICATION_WORK_NAME = "DailyNotificationWork"
        const val NOTIFICATION_HOUR = 21
        const val REPEAT_INTERVAL_HOURS = 24L
    }
}