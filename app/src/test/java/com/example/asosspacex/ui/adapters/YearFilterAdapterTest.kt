package com.example.asosspacex.ui.adapters

import com.example.asosspacex.ui.viewmodels.listitems.YearFilterListItemViewModel
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class YearFilterAdapterTest {

    private val adapterHelper: AdapterHelper = mockk(relaxed = true)

    private val yearFilterListItemViewModelOne = YearFilterListItemViewModel(1, true)
    private val yearFilterListItemViewModelTwo = YearFilterListItemViewModel(2, false)

    private val subject = YearFilterAdapter(adapterHelper)

    @Test
    fun getExcludedYears_returnsCorrectYears() {
        subject.setYears(arrayListOf(yearFilterListItemViewModelOne, yearFilterListItemViewModelTwo))

        val years = subject.getExcludedYears()

        assertEquals(1, years.size)
        assertEquals(2, years[0])
    }
}