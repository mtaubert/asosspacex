package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.CompanyInfoDao
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.networking.responses.CompanyInfoResponse
import com.example.asosspacex.networking.services.CompanyInfoService
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.testhelpers.TestSchedulersProvider
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

class CompanyInfoRepositoryTest {

    private val retrofit: Retrofit = mockk(relaxed = true)
    private val companyInfoDao: CompanyInfoDao = mockk(relaxed = true)
    private val schedulersProvider: TestSchedulersProvider = TestSchedulersProvider()
    private val calendarProvider: CalendarProvider = mockk(relaxed = true)

    private val companyInfoService: CompanyInfoService = mockk(relaxed = true)

    private val apiResponse: CompanyInfoResponse = CompanyInfoResponse("name", "founder", 2022, 10, 10, 100000000L)
    private val dbEntity: CompanyInfoEntity = mockk()
    private val dbResponse: ArrayList<CompanyInfoEntity> = arrayListOf(dbEntity)

    private val companyInfoEntitySlot = slot<CompanyInfoEntity>()

    private val subject = CompanyInfoRepository(
        retrofit,
        companyInfoDao,
        schedulersProvider,
        calendarProvider
    )

    @Before
    fun setUp() {
        every { calendarProvider.timeInMillis() } returns 3600000L //1 Hour
        every { dbEntity.dt } returns 0L

        every { retrofit.create(CompanyInfoService::class.java) } returns companyInfoService
        every { companyInfoService.getCompanyInfo() } returns Single.just(apiResponse)
        every { companyInfoDao.getAll() } returns Single.just(dbResponse)
    }

    @Test
    fun getCompanyInfo_returnsApiData_whenFreshDBDataUnavailable() {
        every { companyInfoDao.getAll() } returns Single.just(arrayListOf())

        val response = subject.getCompanyInfo()

        val testObserver = TestObserver<CompanyInfoEntity>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val companyInfoEntity = testObserver.values()[0]

        verify { companyInfoDao.insertCompanyInfo(capture(companyInfoEntitySlot)) }

        assertEquals(3600000L, companyInfoEntitySlot.captured.dt)
        assertEquals(3600000L, companyInfoEntity.dt)
    }

    @Test
    fun getCompanyInfo_returnsDbData_whenFreshDBDataAvailable() {
        val response = subject.getCompanyInfo()

        val testObserver = TestObserver<CompanyInfoEntity>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        val companyInfoEntity = testObserver.values()[0]

        verify(exactly = 0) { companyInfoDao.insertCompanyInfo(any()) }

        assertEquals(0L, companyInfoEntity.dt)
    }
}