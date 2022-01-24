package com.example.asosspacex.networking.services

import com.example.asosspacex.networking.responses.LaunchesResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface LaunchesService {
    @GET("/v5/launches")
    fun getLaunches(): Single<List<LaunchesResponse>>
}