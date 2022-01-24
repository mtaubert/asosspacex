package com.example.asosspacex.ui.viewmodels.listitems

import com.example.asosspacex.events.OpenLinkEvent
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.greenrobot.eventbus.EventBus
import org.junit.Test

class LinkListItemViewModelTest {

    private val eventBus: EventBus = mockk(relaxed = true)
    private val text = "linkText"
    private val link = "https://en.wikipedia.org/wiki/Wikipedia"

    private val openLinkEventSlot = slot< OpenLinkEvent>()

    @Test
    fun onOpenLink_eventBusPostOpenLinkEvent() {
        val subject = LinkListItemViewModel(
            eventBus,
            text,
            link
        )

        assertEquals("linkText", subject.linkText.get())

        subject.openLink(mockk())

        verify { eventBus.post(capture(openLinkEventSlot)) }
        val openLinkEvent = openLinkEventSlot.captured
        assertEquals("https://en.wikipedia.org/wiki/Wikipedia", openLinkEvent.url)
    }
}