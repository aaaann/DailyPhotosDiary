package com.annevonwolffen.dailyphotosdiary.di

interface FeaturesContainer {
    fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<Dependency>>)

    fun <T : Dependency> getFeature(featureKey: Class<T>): T
}