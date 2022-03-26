package com.annevonwolffen.preferences_api

import com.annevonwolffen.di.Dependency

interface PreferencesApi : Dependency {
    val preferencesManager: PreferencesManager
}