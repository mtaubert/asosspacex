package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.RocketsDao
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.networking.responses.RocketResponse
import com.example.asosspacex.networking.services.RocketsService
import com.example.asosspacex.providers.CalendarProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RocketsRepository @Inject constructor(
    retrofit: Retrofit,
    val rocketsDao: RocketsDao,
    private val calendarProvider: CalendarProvider
) {
    private val rocketsService by lazy {
        retrofit.create(RocketsService::class.java)
    }

    fun getRockets(): Single<HashMap<String, RocketEntity>> {
        return Observable.concat(getDbRockets(), getApiRockets())
            .firstOrError()
    }

    private fun getApiRockets(): Observable<HashMap<String, RocketEntity>> {
        return rocketsService.getAllRockets()
            .map(this::mapAllRocketsResponse)
            .doOnSuccess {
                it.forEach { (_, rocketEntity) -> rocketsDao.insertRocket(rocketEntity) }
            }
            .toObservable()
            .doOnError { it.printStackTrace() }

    }

    private fun mapRocketResponse(response: RocketResponse): RocketEntity {
        return RocketEntity(
            response.id,
            response.name,
            response.type,
            calendarProvider.timeInMillis()
        )
    }

    private fun mapAllRocketsResponse(responses: ArrayList<RocketResponse>): HashMap<String, RocketEntity> {
        val rocketEntityHashMap = HashMap<String, RocketEntity>()
        responses.forEach {
            rocketEntityHashMap[it.id] = mapRocketResponse(it)
        }
        return rocketEntityHashMap
    }

    private fun getDbRockets(): Observable<HashMap<String, RocketEntity>> {
        return rocketsDao.getAll()
            .filter { it.isNotEmpty() }
            .map {
                val map = hashMapOf<String, RocketEntity>()
                it.forEach { rocketEntity ->
                    map[rocketEntity.id] = rocketEntity
                }
                return@map map
            }
            .filter { it.isNotEmpty() }
            .filter(this::isNotStale)
            .toObservable()
            .doOnError { it.printStackTrace() }

    }

    private fun isNotStale(dbDataMap: HashMap<String, RocketEntity>): Boolean {
        val entity = dbDataMap[dbDataMap.keys.first()]
        val timeDiff = calendarProvider.timeInMillis() - entity!!.dt
        return TimeUnit.MILLISECONDS.toHours(timeDiff) < 24
    }
}