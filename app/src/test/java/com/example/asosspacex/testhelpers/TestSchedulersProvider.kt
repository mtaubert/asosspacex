package com.example.asosspacex.testhelpers

import com.example.asosspacex.providers.BaseSchedulersProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


class TestSchedulersProvider(): BaseSchedulersProvider {
    override fun ui(): Scheduler = Schedulers.trampoline()
    override fun io(): Scheduler = Schedulers.trampoline()
}