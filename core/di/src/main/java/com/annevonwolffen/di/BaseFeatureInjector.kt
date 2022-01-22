package com.annevonwolffen.di

abstract class BaseFeatureInjector<T : Dependency>(private val featuresContainer: FeaturesContainer) :
    FeatureInjector<T> {

    private var featureInstance: T? = null

    override fun getFeature(): T = featureInstance ?: buildFeature().also { featureInstance = it }

    protected abstract fun buildFeature(): T

    protected fun <D : Dependency> getDependency(featureKey: Class<D>): D = featuresContainer.getFeature(featureKey)
}