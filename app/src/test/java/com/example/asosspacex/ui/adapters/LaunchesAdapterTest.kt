package com.example.asosspacex.ui.adapters

import com.example.asosspacex.models.Filters
import com.example.asosspacex.ui.viewmodels.SuccessFilter
import com.example.asosspacex.ui.viewmodels.listitems.LaunchListItemViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class LaunchesAdapterTest {

    private val adapterHelper: AdapterHelper = mockk(relaxed = true)

    private val launchOne: LaunchListItemViewModel = mockk(relaxed = true)
    private val launchTwo: LaunchListItemViewModel = mockk(relaxed = true)
    private val launches = arrayListOf(
        launchOne,
        launchTwo
    )

    private val subject = LaunchesAdapter(adapterHelper)

    @Before
    fun setup() {
        every { launchOne.launchYear } returns 1
        every { launchOne.getLaunchTime() } returns 1L
        every { launchOne.wasSuccessful() } returns true
        every { launchTwo.launchYear } returns 2
        every { launchTwo.getLaunchTime() } returns 2L
        every { launchTwo.wasSuccessful() } returns false
    }

    @Test
    fun onSetLaunches_withDefaultFilters_showsCorrectList() {
        subject.setLaunches(launches)

        assertEquals(launches, subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }

    @Test
    fun onSetFilters_withDescendingLaunchTimeFilter_showsCorrectList() {
        subject.setLaunches(launches)

        subject.setFilters(Filters(false, SuccessFilter.ALL, arrayListOf()))

        assertEquals(arrayListOf(launchTwo, launchOne), subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }

    @Test
    fun onSetFilters_withSuccessOnlyFilter_showsCorrectList() {
        subject.setLaunches(launches)

        subject.setFilters(Filters(true, SuccessFilter.SUCCESS_ONLY, arrayListOf()))

        assertEquals(arrayListOf(launchOne), subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }

    @Test
    fun onSetFilters_withFailureOnlyFilter_showsCorrectList() {
        subject.setLaunches(launches)

        subject.setFilters(Filters(true, SuccessFilter.FAILURE_ONLY, arrayListOf()))

        assertEquals(arrayListOf(launchTwo), subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }

    @Test
    fun onSetFilters_withYearFilter_showsCorrectList() {
        subject.setLaunches(launches)

        subject.setFilters(Filters(true, SuccessFilter.ALL, arrayListOf(2)))

        assertEquals(arrayListOf(launchOne), subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }

    @Test
    fun onSetFilters_whenAllLaunchesFilteredOut_showsCorrectList() {
        subject.setLaunches(launches)

        subject.setFilters(Filters(true, SuccessFilter.FAILURE_ONLY, arrayListOf(2)))

        assertEquals(arrayListOf<LaunchListItemViewModel>(), subject.visibleLaunches)
        verify { adapterHelper.notifyDataSetChanged(subject) }
    }
}