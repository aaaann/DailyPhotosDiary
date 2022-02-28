package com.annevonwolffen.dailyphotosdiary

import android.app.Application
import androidx.work.Configuration
import com.annevonwolffen.dailyphotosdiary.di.DaggerFeatureInjectorsComponent
import com.annevonwolffen.di.FeatureProvider
import com.annevonwolffen.di.FeaturesContainerImpl

class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        FeaturesContainerImpl().also {
            it.setFeatures(DaggerFeatureInjectorsComponent.factory().create(this, it).featureInjectors)
            FeatureProvider.init(it)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }
}