package com.example.asosspacex.ui.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.asosspacex.R
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.networking.repositories.CompanyInfoRepository
import com.example.asosspacex.providers.BaseSchedulersProvider
import com.example.asosspacex.providers.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    schedulersProvider: BaseSchedulersProvider,
    companyInfoRepository: CompanyInfoRepository
) : ViewModel() {

    val showLoadingSpinner = ObservableField(true)
    val companyInfoText = ObservableField("")

    init {
        showLoadingSpinner.set(true)

        companyInfoRepository.getCompanyInfo()
            .subscribeOn(schedulersProvider.io())
            .doOnError(this::errorGettingCompanyInfo)
            .subscribe(this::updateCompanyInfo, this::errorGettingCompanyInfo)

    }

    private fun updateCompanyInfo(companyInfo: CompanyInfoEntity) {
        showLoadingSpinner.set(false)
        companyInfoText.set(resourcesProvider.getString(R.string.company_details).format(
            companyInfo.name,
            companyInfo.founder,
            companyInfo.founded,
            companyInfo.employees,
            companyInfo.launch_sites,
            formatValuation(companyInfo.valuation)
        ))
    }

    private fun formatValuation(valuation: Long): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("USD")

        return format.format(valuation)
    }

    private fun errorGettingCompanyInfo(t: Throwable) {
        showLoadingSpinner.set(false)
        t.printStackTrace()
        companyInfoText.set(resourcesProvider.getString(R.string.company_details_missing))
    }
}