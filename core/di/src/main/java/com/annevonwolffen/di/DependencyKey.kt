package com.annevonwolffen.di

import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class DependencyKey(val value: KClass<out Dependency>)
