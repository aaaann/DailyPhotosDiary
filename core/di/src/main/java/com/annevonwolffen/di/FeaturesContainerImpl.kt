package com.annevonwolffen.di

import java.lang.IllegalStateException

class FeaturesContainerImpl : com.annevonwolffen.di.FeaturesContainer {

    private var featureInjectors: Map<Class<out com.annevonwolffen.di.Dependency>, com.annevonwolffen.di.FeatureInjector<com.annevonwolffen.di.Dependency>> = emptyMap()

    override fun setFeatures(featureInjectors: Map<Class<out com.annevonwolffen.di.Dependency>, com.annevonwolffen.di.FeatureInjector<com.annevonwolffen.di.Dependency>>) {
        this.featureInjectors = featureInjectors
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : com.annevonwolffen.di.Dependency> getFeature(featureKey: Class<T>): T {
        return featureInjectors[featureKey] as T?
            ?: throw IllegalStateException("No dependency $featureKey in ${featureInjectors.keys}")
    }
}