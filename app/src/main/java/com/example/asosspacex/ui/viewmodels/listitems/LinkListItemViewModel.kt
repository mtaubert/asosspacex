package com.example.asosspacex.ui.viewmodels.listitems

import android.view.View
import androidx.databinding.ObservableField
import com.example.asosspacex.events.OpenLinkEvent
import org.greenrobot.eventbus.EventBus

class LinkListItemViewModel (
    private val eventBus: EventBus,
    text: String,
    private val link: String?
    ) {

    val linkText = ObservableField(text)

    fun openLink(v: View) {
        eventBus.post(OpenLinkEvent(link))
    }
}