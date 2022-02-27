package com.annevonwolffen.settings_impl.di

import com.annevonwolffen.di.Dependency
import com.annevonwolffen.settings_impl.domain.SettingsInteractor

interface SettingsInternalApi : Dependency {
    val settingsInteractor: SettingsInteractor
}