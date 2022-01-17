package com.annevonwolffen.di

interface FeatureInjector<T : com.annevonwolffen.di.Dependency> {
    fun getFeature(): T
}
