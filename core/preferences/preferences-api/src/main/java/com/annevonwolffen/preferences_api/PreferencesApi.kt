package com.annevonwolffen.preferences_api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.annevonwolffen.di.Dependency

interface PreferencesApi : Dependency {
    val dataStore: DataStore<Preferences>
}