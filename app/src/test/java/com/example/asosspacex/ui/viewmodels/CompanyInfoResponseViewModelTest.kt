package com.example.asosspacex.ui.viewmodels

import com.example.asosspacex.R
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.networking.repositories.CompanyInfoRepository
import com.example.asosspacex.providers.ResourcesProvider
import com.example.asosspacex.testhelpers.TestSchedulersProvider
import io.mockk.*
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CompanyInfoResponseViewModelTest {

    private val resourcesProvider: ResourcesProvider = mockk()
    private val companyInfoRepository: CompanyInfoRepository = mockk()
    private val schedulersProvider: TestSchedulersProvider = TestSchedulersProvider()

    @Before
    fun setup() {
        every { resourcesProvider.getString(R.string.company_details) } returns "%s was founded by %s in %d. It has now %d employees, %d launch sites, and is valued at %s"
        every { resourcesProvider.getString(R.string.company_details_missing) } returns "SpaceX company details missing"
        every { companyInfoRepository.getCompanyInfo() } returns Single.just(
            CompanyInfoEntity(
                "name",
                "founder",
                2020,
                10,
                10,
                100000000L,
                0L
            )
        )
    }

    @Test
    fun onInit_requestSuccess_companyInfoTextSetCorrectly() {
        val subject = getSubject()

        assertEquals("name was founded by founder in 2020. It has now 10 employees, 10 launch sites, and is valued at US$100,000,000", subject.companyInfoText.get())
    }

    @Test
    fun onInit_requestFailure_companyInfoTextSetCorrectly() {
        every { companyInfoRepository.getCompanyInfo() } returns Single.error(Throwable())

        val subject = getSubject()

        assertEquals("SpaceX company details missing", subject.companyInfoText.get())
    }

    private fun getSubject(): CompanyInfoViewModel {
        return CompanyInfoViewModel(
            resourcesProvider,
            schedulersProvider,
            companyInfoRepository
        )
    }
}