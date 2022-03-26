package com.annevonwolffen.preferences_impl.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesManager
import dagger.Module
import dagger.Provides

private const val USER_PREFERENCES = "user_preferences"

@Module
object PreferencesModule {
    @PerFeature
    @Provides
    fun providePreferencesManager(appContext: Context): PreferencesManager {
        val dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
        return PreferencesManagerImpl(dataStore)
    }
}