package com.annevonwolffen.settings_impl.domain.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyNotificationWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doing work...")

        return Result.success()
    }

    private companion object {
        const val TAG = "DailyNotificationWorker"
    }
}