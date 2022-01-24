package com.example.asosspacex.ui.viewmodels

import com.example.asosspacex.R
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.events.ToastEvent
import com.example.asosspacex.events.UpdateFiltersYearEvent
import com.example.asosspacex.models.defaultFilters
import com.example.asosspacex.networking.repositories.LaunchesRepository
import com.example.asosspacex.networking.repositories.RocketsRepository
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.providers.ResourcesProvider
import com.example.asosspacex.testhelpers.TestSchedulersProvider
import com.example.asosspacex.ui.adapters.LaunchesAdapter
import com.example.asosspacex.ui.viewmodels.listitems.LaunchListItemViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test

class LaunchesListViewModelTest {

    private val adapter: LaunchesAdapter = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)
    private val launchesRepository: LaunchesRepository = mockk()
    private val rocketsRepository: RocketsRepository = mockk()
    private val resourcesProvider: ResourcesProvider = mockk(relaxed = true)
    private val schedulersProvider: TestSchedulersProvider = TestSchedulersProvider()
    private val calendarProvider: CalendarProvider = mockk(relaxed = true)


    private val mockLaunchEntity: LaunchEntity = mockk(relaxed = true)
    private val mockRocketEntity: RocketEntity = mockk(relaxed = true)

    private val launchListItemViewModelListSlot = slot<ArrayList<LaunchListItemViewModel>>()
    private val updateFiltersYearEventSlot = slot<UpdateFiltersYearEvent>()
    private val toastEventSlot = slot<ToastEvent>()

    @Before
    fun setup() {
        every { mockLaunchEntity.id } returns "launchID"
        every { mockLaunchEntity.rocketId } returns "rocketID"
        every { mockLaunchEntity.date_unix } returns 1642726800L //21-01-2022 at 1:00:00
        every { launchesRepository.getLaunches() } returns Single.just(arrayListOf(mockLaunchEntity))
        every { rocketsRepository.getRockets() } returns Single.just(hashMapOf("rocketID" to mockRocketEntity))
    }

    @Test
    fun onInit_allRequestsSuccess_launchesLoadedAndShown() {
        val subject = getSubject()

        verify { adapter.setLaunches(capture(launchListItemViewModelListSlot)) }
        val launchListItemViewModelList = launchListItemViewModelListSlot.captured
        assertEquals(1, launchListItemViewModelList.size)
        assertEquals("launchID", launchListItemViewModelList[0].getLaunchId())

        verify { eventBus.post(capture(updateFiltersYearEventSlot)) }
        val filtersYearEvent = updateFiltersYearEventSlot.captured
        assertEquals(arrayListOf(2022), filtersYearEvent.years)
    }

    @Test
    fun onInit_launchesRequestsFailure_showsError() {
        every { launchesRepository.getLaunches() } returns Single.error(Throwable())
        every { rocketsRepository.getRockets() } returns Single.just(hashMapOf("rocketID" to mockRocketEntity))

        val subject = getSubject()

        verify { eventBus.post(capture(toastEventSlot)) }
        val toastEvent = toastEventSlot.captured
        assertEquals(R.string.could_not_load_launches_data, toastEvent.stringRes)
    }

    @Test
    fun onInit_rocketsRequestsFailure_showsError() {
        every { launchesRepository.getLaunches() } returns Single.just(arrayListOf(mockLaunchEntity))
        every { rocketsRepository.getRockets() } returns Single.error(Throwable())

        val subject = getSubject()

        verify { eventBus.post(capture(toastEventSlot)) }
        val toastEvent = toastEventSlot.captured
        assertEquals(R.string.could_not_load_launches_data, toastEvent.stringRes)
    }

    @Test
    fun onSetFilters_adapterRecivesFilters() {
        val filters = defaultFilters

        val subject = getSubject()
        subject.filterLaunches(defaultFilters)

        verify { adapter.setFilters(filters) }
    }

    private fun getSubject(): LaunchesListViewModel {
        return LaunchesListViewModel(
            adapter,
            eventBus,
            launchesRepository,
            rocketsRepository,
            resourcesProvider,
            schedulersProvider,
            calendarProvider
        )
    }
}