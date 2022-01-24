package com.example.asosspacex.networking.services

import com.example.asosspacex.networking.responses.RocketResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface RocketsService {
    @GET("/v4/rockets/{id}")
    fun getRocket(@Path("id") id: String): Observable<RocketResponse>

    @GET("/v4/rockets")
    fun getAllRockets(): Single<ArrayList<RocketResponse>>
}