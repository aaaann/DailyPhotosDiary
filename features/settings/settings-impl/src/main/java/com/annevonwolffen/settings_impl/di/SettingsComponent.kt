package com.annevonwolffen.settings_impl.di

import android.content.Context
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesApi
import dagger.BindsInstance
import dagger.Component

@PerFeature
@Component(modules = [SettingsModule::class], dependencies = [PreferencesApi::class])
internal interface SettingsComponent : SettingsInternalApi {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, preferencesApi: PreferencesApi): SettingsComponent
    }
}