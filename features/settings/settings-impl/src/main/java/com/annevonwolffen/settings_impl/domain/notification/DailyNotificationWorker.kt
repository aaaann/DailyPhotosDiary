package com.annevonwolffen.settings_impl.domain.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.gallery_api.di.GalleryExternalApi

class DailyNotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doing work...")
        if (getFeature(GalleryExternalApi::class.java).imagesExternalInteractor
                .hasImagesForToday(TEST_FOLDER)
        ) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        // val intent = Intent(appContext, TasksActivity::class.java).apply {
        //     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // }
        // val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)
        //
        // val manager: NotificationManager =
        //     appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //     val notificationChannel = createChannel()
        //     manager.createNotificationChannel(notificationChannel)
        // }
        //
        // val notification = createNotificationBuilder(pendingIntent)
        // manager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(): NotificationChannel {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL,
            "DailyPhotosDiary",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(0, 100, 200, 300)
        notificationChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        )
        return notificationChannel
    }

    private fun createNotificationBuilder(pendingIntent: PendingIntent): Notification {
        val notificationBuilder = NotificationCompat.Builder(
            appContext,
            NOTIFICATION_CHANNEL
        )
            .setChannelId(NOTIFICATION_CHANNEL)
            .setContentIntent(pendingIntent)
            // .setContentTitle(
            //     appContext.getString(
            //
            //     )
            // )
            // .setContentText(appContext.getString(R.string.notification_text))
            // .setSmallIcon(R.drawable.ic_check_24dp)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVibrate(longArrayOf(0, 100, 200, 300))

        return notificationBuilder.build()
    }

    private companion object {
        const val TAG = "DailyNotificationWorker"
        const val TEST_FOLDER = "testfolder"
        private const val NOTIFICATION_CHANNEL = "DailyPhotosDiary_channel_1"
    }
}