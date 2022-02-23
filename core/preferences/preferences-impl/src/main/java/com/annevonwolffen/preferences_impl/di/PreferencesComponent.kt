package com.annevonwolffen.preferences_impl.di

import android.content.Context
import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.preferences_api.PreferencesApi
import dagger.BindsInstance
import dagger.Component

@PerFeature
@Component(modules = [PreferencesModule::class])
interface PreferencesComponent : PreferencesApi {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): PreferencesComponent
    }
}