package com.annevonwolffen.settings_impl.di

import android.content.Context
import androidx.work.WorkManager
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesManager
import com.annevonwolffen.settings_impl.R
import com.annevonwolffen.settings_impl.data.SettingsRepositoryImpl
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import com.annevonwolffen.settings_impl.domain.SettingsInteractorImpl
import com.annevonwolffen.settings_impl.domain.notification.NotificationWorkManager
import com.annevonwolffen.settings_impl.domain.notification.NotificationWorkManagerImpl
import dagger.Module
import dagger.Provides

@Module
object SettingsModule {

    @PerFeature
    @Provides
    fun provideSettingsInteractor(preferencesManager: PreferencesManager, context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(
                preferencesManager,
                context.getString(R.string.daily_notification_key)
            )
        )
    }

    @PerFeature
    @Provides
    fun provideNotificationWorkManager(appContext: Context): NotificationWorkManager {
        return NotificationWorkManagerImpl(WorkManager.getInstance(appContext))
    }
}