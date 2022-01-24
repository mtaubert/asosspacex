package com.example.asosspacex.di

import com.example.asosspacex.providers.BaseSchedulersProvider
import com.example.asosspacex.providers.SchedulersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SchedulersModule {
    @Binds
    abstract fun bindsBaseSchedulersProvider(schedulersProvider: SchedulersProvider): BaseSchedulersProvider
}