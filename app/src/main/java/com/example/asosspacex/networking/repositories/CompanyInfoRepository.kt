package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.CompanyInfoDao
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.networking.responses.CompanyInfoResponse
import com.example.asosspacex.networking.services.CompanyInfoService
import com.example.asosspacex.providers.BaseSchedulersProvider
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.providers.SchedulersProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CompanyInfoRepository @Inject constructor(
    retrofit: Retrofit,
    private val companyInfoDao: CompanyInfoDao,
    private val schedulersProvider: BaseSchedulersProvider,
    private val calendarProvider: CalendarProvider
) {
    private val companyInfoService: CompanyInfoService by lazy {
        retrofit.create(CompanyInfoService::class.java)
    }

    fun getCompanyInfo(): Single<CompanyInfoEntity> {
        return Observable.concat(getDbCompanyInfo(), getApiCompanyInfo())
            .firstOrError()
    }

    private fun getApiCompanyInfo(): Observable<CompanyInfoEntity> {
        return companyInfoService.getCompanyInfo()
            .map(this::mapCompanyInfoResponse)
            .doOnSuccess { companyInfoDao.insertCompanyInfo(it) }
            .toObservable()
            .doOnError { it.printStackTrace() }
    }

    private fun getDbCompanyInfo(): Observable<CompanyInfoEntity> {
        return companyInfoDao.getAll()
            .filter { it.isNotEmpty() }
            .map { it.first() }
            .filter(this::isNotStale)
            .subscribeOn(schedulersProvider.io())
            .toObservable()
            .doOnError { it.printStackTrace() }
    }

    private fun mapCompanyInfoResponse(response: CompanyInfoResponse): CompanyInfoEntity {
        return CompanyInfoEntity(
            response.name,
            response.founder,
            response.founded,
            response.employees,
            response.launch_sites,
            response.valuation,
            calendarProvider.timeInMillis()
        )
    }

    private fun isNotStale(entity: CompanyInfoEntity): Boolean {
        val timeDiff = calendarProvider.timeInMillis() - entity.dt
        return TimeUnit.MILLISECONDS.toHours(timeDiff) < 24
    }
}