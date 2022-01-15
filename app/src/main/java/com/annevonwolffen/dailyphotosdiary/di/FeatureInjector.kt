package com.annevonwolffen.dailyphotosdiary.di

interface FeatureInjector<T : Dependency> {
    fun getFeature(): T
}
