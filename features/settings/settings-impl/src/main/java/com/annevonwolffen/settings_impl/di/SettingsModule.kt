package com.annevonwolffen.settings_impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.settings_impl.R
import com.annevonwolffen.settings_impl.data.SettingsRepositoryImpl
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import com.annevonwolffen.settings_impl.domain.SettingsInteractorImpl
import dagger.Module
import dagger.Provides

@Module
object SettingsModule {

    @PerFeature
    @Provides
    fun provideSettingsInteractor(dataStore: DataStore<Preferences>, context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(
                dataStore,
                context.getString(R.string.daily_notification_key)
            )
        )
    }
}