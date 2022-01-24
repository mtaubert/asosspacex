package com.example.asosspacex.providers

import io.reactivex.rxjava3.core.Scheduler

interface BaseSchedulersProvider {
    abstract fun ui(): Scheduler
    abstract fun io(): Scheduler
}