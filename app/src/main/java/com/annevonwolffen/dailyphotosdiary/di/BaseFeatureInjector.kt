package com.annevonwolffen.dailyphotosdiary.di

abstract class BaseFeatureInjector<T : Dependency>(private val featuresContainer: FeaturesContainer) : FeatureInjector<T> {

    private val featureInstance: T? = null

    override fun getFeature(): T = featureInstance ?: buildFeature()

    protected abstract fun buildFeature(): T

    protected fun <D: Dependency> getDependency(featureKey: Class<D>): D = featuresContainer.getFeature(featureKey)
}