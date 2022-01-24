package com.example.asosspacex.ui.viewmodels

import com.example.asosspacex.events.ShowLinksEvent
import com.example.asosspacex.ui.adapters.LinksAdapter
import org.greenrobot.eventbus.EventBus

class LinksPopupViewModel(
    val adapter: LinksAdapter = LinksAdapter()
    ) {

    fun setLinks(linksEvent: ShowLinksEvent) {
        adapter.setLinks(linksEvent.links)
    }
}