package com.annevonwolffen.di

interface FeaturesContainer {
    fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>>)

    fun <T : Dependency> getFeature(featureKey: Class<T>): T
}