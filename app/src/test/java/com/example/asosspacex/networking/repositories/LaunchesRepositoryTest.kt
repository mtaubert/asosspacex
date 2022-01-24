package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.LaunchesDao
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.networking.responses.LaunchesResponse
import com.example.asosspacex.networking.services.LaunchesService
import com.example.asosspacex.providers.CalendarProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class LaunchesRepositoryTest {

    private val retrofit: Retrofit = mockk(relaxed = true)
    private val launchesDao: LaunchesDao = mockk(relaxed = true)
    private val calendarProvider: CalendarProvider = mockk(relaxed = true)
    private val launchesService: LaunchesService = mockk(relaxed = true)

    private val dbEntity: LaunchEntity = mockk(relaxed = true)
    private val apiResponse: LaunchesResponse = mockk(relaxed = true)

    private val launchEntityListSlot = slot<ArrayList<LaunchEntity>>()


    private val subject = LaunchesRepository(
        retrofit,
        launchesDao,
        calendarProvider
    )

    @Before
    fun setup() {
        every { calendarProvider.timeInMillis() } returns 10000L //10 Seconds
        every { dbEntity.dt } returns 0L
        every { apiResponse.id } returns "id"

        every { retrofit.create(LaunchesService::class.java) } returns launchesService
        every { launchesDao.getAll() } returns Single.just(arrayListOf(dbEntity))
        every { launchesService.getLaunches() } returns Single.just(arrayListOf(apiResponse))
    }

    @Test
    fun getLaunches_returnsApiData_whenFreshDBDataUnavailable() {
        every { launchesDao.getAll() } returns Single.just(arrayListOf())

        val response = subject.getLaunches()

        val testObserver = TestObserver<List<LaunchEntity>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val launchEntity = testObserver.values()[0]

        verify { launchesDao.insertAll(capture(launchEntityListSlot)) }

        assertEquals(10000L, launchEntityListSlot.captured[0].dt)
        assertEquals("id", launchEntityListSlot.captured[0].id)
        assertEquals(10000L, launchEntity[0].dt)
    }

    @Test
    fun getLaunches_returnsDbData_whenFreshDBDataAvailable() {
        val response = subject.getLaunches()

        val testObserver = TestObserver<List<LaunchEntity>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val launchEntity = testObserver.values()[0]

        verify(exactly = 0) { launchesDao.insertAll(any()) }

        assertEquals(0L, launchEntity[0].dt)
    }
}