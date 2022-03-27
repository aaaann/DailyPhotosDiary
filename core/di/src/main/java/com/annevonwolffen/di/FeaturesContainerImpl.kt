package com.annevonwolffen.di

import kotlin.reflect.KClass

class FeaturesContainerImpl : FeaturesContainer {

    private var featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>> = emptyMap()

    override fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>>) {
        this.featureInjectors = featureInjectors
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Dependency> getFeature(featureKey: KClass<T>): T {
        return featureInjectors[featureKey.java]?.getFeature() as? T
            ?: throw IllegalStateException("No dependency $featureKey in ${featureInjectors.keys}")
    }
}