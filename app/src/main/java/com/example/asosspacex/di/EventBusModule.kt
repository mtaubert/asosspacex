package com.example.asosspacex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventBusModule {
    @Provides
    @Singleton
    fun providesEventBus(): EventBus {
        return EventBus.getDefault()
    }
}