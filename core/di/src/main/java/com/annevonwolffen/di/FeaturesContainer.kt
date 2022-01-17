package com.annevonwolffen.di

interface FeaturesContainer {
    fun setFeatures(featureInjectors: Map<Class<out com.annevonwolffen.di.Dependency>, com.annevonwolffen.di.FeatureInjector<com.annevonwolffen.di.Dependency>>)

    fun <T : com.annevonwolffen.di.Dependency> getFeature(featureKey: Class<T>): T
}