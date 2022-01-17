package com.annevonwolffen.dailyphotosdiary

import android.app.Application
import com.annevonwolffen.dailyphotosdiary.di.DaggerFeatureInjectorsComponent
import com.annevonwolffen.di.FeaturesContainerImpl

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FeaturesContainerImpl().also {
            it.setFeatures(DaggerFeatureInjectorsComponent.factory().create(this, it).featureInjectors)
        }
    }
}