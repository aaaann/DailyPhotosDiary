package com.annevonwolffen.di

import kotlin.reflect.KClass

interface FeaturesContainer {
    fun setFeatures(featureInjectors: Map<Class<out Dependency>, FeatureInjector<out Dependency>>)

    fun <T : Dependency> getFeature(featureKey: KClass<T>): T
}