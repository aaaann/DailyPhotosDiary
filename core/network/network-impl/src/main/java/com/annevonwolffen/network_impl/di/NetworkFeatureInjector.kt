package com.annevonwolffen.network_impl.di

import com.annevonwolffen.di.BaseFeatureInjector
import com.annevonwolffen.di.FeaturesContainer
import com.annevonwolffen.network_api.NetworkApi
import javax.inject.Inject

class NetworkFeatureInjector @Inject constructor(featuresContainer: FeaturesContainer) :
    BaseFeatureInjector<NetworkApi>(featuresContainer) {
    override fun buildFeature(): NetworkApi {
        return DaggerNetworkComponent.factory().create()
    }
}