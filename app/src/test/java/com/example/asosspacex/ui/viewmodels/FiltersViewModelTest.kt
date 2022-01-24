package com.example.asosspacex.ui.viewmodels

import android.widget.RadioButton
import com.example.asosspacex.R
import com.example.asosspacex.events.FiltersAppliedEvent
import com.example.asosspacex.models.Filters
import com.example.asosspacex.models.defaultFilters
import com.example.asosspacex.ui.adapters.YearFilterAdapter
import com.example.asosspacex.ui.viewmodels.listitems.YearFilterListItemViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test

class FiltersViewModelTest {

    private val adapter: YearFilterAdapter = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)

    private val filtersAppliedEventSlot = slot<FiltersAppliedEvent>()
    private val yearFilterListItemViewModelListSlot = slot<ArrayList<YearFilterListItemViewModel>>()

    private val subject = FiltersViewModel(
        adapter,
        eventBus
    )

    @Before
    fun setup() {
        every { adapter.getExcludedYears() } returns arrayListOf()
    }

    @Test
    fun onApplyFilters_withDefaultFilters_eventBusPostsCorrectFilters() {
        subject.applyFilters(mockk())

        verify { eventBus.post(capture(filtersAppliedEventSlot)) }
        assertEquals(defaultFilters, filtersAppliedEventSlot.captured.filters)
    }

    @Test
    fun onApplyFilters_withChangedFilters_eventBusPostsCorrectFilters() {
        every { adapter.getExcludedYears() } returns arrayListOf(2022)
        val orderingRadioButton: RadioButton = mockk()
        every { orderingRadioButton.id } returns R.id.order_descending
        val successRadioButton: RadioButton = mockk()
        every { successRadioButton.id } returns R.id.failure

        subject.orderingRadioButtonsClicked(orderingRadioButton)
        subject.successRadioButtonsClicked(successRadioButton)
        subject.applyFilters(mockk())

        verify { eventBus.post(capture(filtersAppliedEventSlot)) }
        assertEquals(Filters(false, SuccessFilter.FAILURE_ONLY, arrayListOf(2022)), filtersAppliedEventSlot.captured.filters)
    }

    @Test
    fun updateYearsList_withNoExcludedYears_updatesAdapter() {
        val years = arrayListOf(2020, 2021, 2022)
        val yearFilterListItemViewModelList = arrayListOf<YearFilterListItemViewModel>()
        years.forEach {
            yearFilterListItemViewModelList.add(YearFilterListItemViewModel(it, true))
        }
        subject.updateYearsList(years)

        verify { adapter.setYears(capture(yearFilterListItemViewModelListSlot)) }
        val list = yearFilterListItemViewModelListSlot.captured
        assertEquals(2020, list[0].year)
        assertEquals(2021, list[1].year)
        assertEquals(2022, list[2].year)
        assertEquals(true, list[0].isChecked.get())
        assertEquals(true, list[1].isChecked.get())
        assertEquals(true, list[2].isChecked.get())
    }

    @Test
    fun updateYearsList_withExcludedYears_updatesAdapter() {
        val years = arrayListOf(2020, 2021, 2022)
        val excludedYears = arrayListOf(2021)

        subject.setExcludedYears(excludedYears)
        subject.updateYearsList(years)

        verify { adapter.setYears(capture(yearFilterListItemViewModelListSlot)) }
        val list = yearFilterListItemViewModelListSlot.captured
        assertEquals(2020, list[0].year)
        assertEquals(2021, list[1].year)
        assertEquals(2022, list[2].year)
        assertEquals(true, list[0].isChecked.get())
        assertEquals(false, list[1].isChecked.get())
        assertEquals(true, list[2].isChecked.get())
    }
}