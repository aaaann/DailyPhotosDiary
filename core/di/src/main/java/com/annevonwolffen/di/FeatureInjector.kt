package com.annevonwolffen.di

interface FeatureInjector<T : Dependency> {
    fun getFeature(): T
}
