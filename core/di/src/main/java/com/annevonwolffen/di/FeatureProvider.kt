package com.annevonwolffen.di

import java.lang.IllegalStateException

object FeatureProvider {

    private var featuresContainer: FeaturesContainer? = null

    fun init(featuresContainer: FeaturesContainer) {
        this.featuresContainer = featuresContainer
    }

    fun <T : Dependency> getFeature(featureKey: Class<T>): T = featuresContainer?.getFeature(featureKey)
        ?: throw IllegalStateException("FeaturesContainer is nit initialized!")
}