package com.annevonwolffen.dailyphotosdiary.di

import java.lang.IllegalStateException

class FeaturesContainerImpl : FeaturesContainer {

    private var featureInjectors: Map<Class<out Dependency>, FeatureInjector<Dependency>> = emptyMap()

    override fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<Dependency>>) {
        this.featureInjectors = featureInjectors
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Dependency> getFeature(featureKey: Class<T>): T {
        return featureInjectors[featureKey] as T?
            ?: throw IllegalStateException("No dependency $featureKey in ${featureInjectors.keys}")
    }
}