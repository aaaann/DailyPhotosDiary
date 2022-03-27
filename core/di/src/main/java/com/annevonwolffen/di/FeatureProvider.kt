package com.annevonwolffen.di

import kotlin.reflect.KClass

object FeatureProvider {

    private var featuresContainer: FeaturesContainer? = null

    fun init(featuresContainer: FeaturesContainer) {
        this.featuresContainer = featuresContainer
    }

    fun <T : Dependency> getFeature(featureKey: KClass<T>): T = featuresContainer?.getFeature(featureKey)
        ?: throw IllegalStateException("FeaturesContainer is not initialized!")

    @Suppress("UNCHECKED_CAST")
    fun <T : Dependency, E : T> getInnerFeature(outerFeatureKey: KClass<T>, innerFeatureKey: KClass<E>): E =
        getFeature(outerFeatureKey) as? E
            ?: throw IllegalStateException("Feature with key [$innerFeatureKey] does not extend feature [$outerFeatureKey]")
}