package com.annevonwolffen.settings_impl.domain.notification

import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorkManagerImpl(private val workManager: WorkManager) : NotificationWorkManager {
    override fun scheduleNotification() {
        workManager
            .enqueueUniqueWork(
                NOTIFICATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<DailyNotificationWorker>()
                    .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                    .build()
            )
    }

    private fun calculateInitialDelay(): Long {
        val currentDate: Calendar = Calendar.getInstance()
        val notificationDate: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (!notificationDate.after(currentDate)) {
            notificationDate.add(Calendar.HOUR_OF_DAY, REPEAT_INTERVAL_HOURS)
        }
        val delay = notificationDate.timeInMillis - currentDate.timeInMillis
        Log.d("NotificationWorkManager", "init delay: $delay")
        return delay
    }

    override fun cancelNotification() {
        workManager.cancelUniqueWork(NOTIFICATION_WORK_NAME)
    }

    private companion object {
        const val NOTIFICATION_WORK_NAME = "DailyNotificationWork"
        const val NOTIFICATION_HOUR = 21
        const val REPEAT_INTERVAL_HOURS = 24
    }
}