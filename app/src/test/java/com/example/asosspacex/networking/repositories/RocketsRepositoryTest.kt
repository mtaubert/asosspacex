package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.RocketsDao
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.networking.responses.RocketResponse
import com.example.asosspacex.networking.services.RocketsService
import com.example.asosspacex.providers.CalendarProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class RocketsRepositoryTest {

    private val retrofit: Retrofit = mockk(relaxed = true)
    private val rocketsDao: RocketsDao = mockk(relaxed = true)
    private val calendarProvider: CalendarProvider = mockk(relaxed = true)
    private val rocketsService: RocketsService = mockk(relaxed = true)

    private val apiResponse: RocketResponse = mockk(relaxed = true)
    private val dbEntity: RocketEntity = mockk()

    private val rocketEntitySlot = slot<RocketEntity>()


    private val subject = RocketsRepository(
        retrofit,
        rocketsDao,
        calendarProvider
    )

    @Before
    fun setUp() {
        every { calendarProvider.timeInMillis() } returns 3600000L //1 Hour
        every { dbEntity.dt } returns 0L
        every { dbEntity.id } returns "id"
        every { apiResponse.id } returns "id"

        every { retrofit.create(RocketsService::class.java) } returns rocketsService
        every { rocketsService.getAllRockets() } returns Single.just(arrayListOf(apiResponse))
        every { rocketsDao.getAll() } returns Single.just(listOf(dbEntity))
    }

    @Test
    fun getRockets_returnsApiData_whenFreshDBDataUnavailable() {
        every { rocketsDao.getAll() } returns Single.just(arrayListOf())

        val response = subject.getRockets()

        val testObserver = TestObserver<HashMap<String, RocketEntity>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val rocketMap = testObserver.values()[0]

        verify(exactly = 1) { rocketsDao.insertRocket(capture(rocketEntitySlot)) }

        TestCase.assertEquals(3600000L, rocketEntitySlot.captured.dt)
        TestCase.assertEquals("id", rocketEntitySlot.captured.id)
        TestCase.assertEquals(3600000L, rocketMap["id"]!!.dt)
    }

    @Test
    fun getRockets_returnsDbData_whenFreshDBDataAvailable() {
        val response = subject.getRockets()

        val testObserver = TestObserver<HashMap<String, RocketEntity>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val rocketMap = testObserver.values()[0]

        verify(exactly = 0) { rocketsDao.insertRocket(any()) }

        TestCase.assertEquals(0L, rocketMap["id"]!!.dt)
    }
}