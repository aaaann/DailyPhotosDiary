package com.annevonwolffen.mainscreen_impl.di

import com.annevonwolffen.di.PerFeature
import com.annevonwolffen.mainscreen_api.MainScreenRouter
import com.annevonwolffen.mainscreen_impl.presentation.MainScreenRouterImpl
import dagger.Binds
import dagger.Module

@Module
internal interface MainScreenModule {

    @PerFeature
    @Binds
    fun bindMainScreenRouter(mainScreenRouterImpl: MainScreenRouterImpl): MainScreenRouter
}