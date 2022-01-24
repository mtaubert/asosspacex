package com.example.asosspacex.ui.viewmodels

import com.example.asosspacex.events.ShowLinksEvent
import com.example.asosspacex.ui.adapters.LinksAdapter
import com.example.asosspacex.ui.viewmodels.listitems.LinkListItemViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LinksPopupViewModelTest {

    private val adapter: LinksAdapter = mockk(relaxed = true)

    @Test
    fun onSetLinks_updatesAdapterLinks() {
        val link: LinkListItemViewModel = mockk()
        val links = arrayListOf(link)
        val showLinksEvent = ShowLinksEvent(links)
        val subject = LinksPopupViewModel(adapter)

        subject.setLinks(showLinksEvent)

        verify { adapter.setLinks(links) }
    }
}