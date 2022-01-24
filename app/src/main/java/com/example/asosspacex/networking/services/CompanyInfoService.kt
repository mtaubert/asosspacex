package com.example.asosspacex.networking.services

import com.example.asosspacex.networking.responses.CompanyInfoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CompanyInfoService {
    @GET("/v4/company")
    fun getCompanyInfo(): Single<CompanyInfoResponse>
}