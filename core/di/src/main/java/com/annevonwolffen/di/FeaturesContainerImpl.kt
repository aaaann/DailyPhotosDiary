package com.annevonwolffen.di

class FeaturesContainerImpl : FeaturesContainer {

    private var featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>> = emptyMap()

    override fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>>) {
        this.featureInjectors = featureInjectors
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Dependency> getFeature(featureKey: Class<T>): T {
        return featureInjectors[featureKey]?.getFeature() as T?
            ?: throw IllegalStateException("No dependency $featureKey in ${featureInjectors.keys}")
    }
}