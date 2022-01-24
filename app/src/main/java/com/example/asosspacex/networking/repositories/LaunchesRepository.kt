package com.example.asosspacex.networking.repositories

import com.example.asosspacex.db.dao.LaunchesDao
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.networking.responses.LaunchesResponse
import com.example.asosspacex.networking.services.LaunchesService
import com.example.asosspacex.providers.CalendarProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    retrofit: Retrofit,
    private val launchesDao: LaunchesDao,
    private val calendarProvider: CalendarProvider,
    private val eventBus: EventBus
) {

    private val launchesService: LaunchesService by lazy {
        retrofit.create(LaunchesService::class.java)
    }

    fun getLaunches(): Single<List<LaunchEntity>> {
        return Observable.concat(getDbLaunches(), getApiLaunches())
            .firstOrError()
    }

    private fun getApiLaunches(): Observable<List<LaunchEntity>> {
        return launchesService.getLaunches()
            .map(this::mapLaunchesResponse)
            .doOnSuccess(launchesDao::insertAll)
            .toObservable()
            .doOnError { it.printStackTrace() }
    }

    private fun getDbLaunches(): Observable<List<LaunchEntity>> {
        return launchesDao.getAll()
            .filter{ it.isNotEmpty() }
            .filter { isNotStale(it.first()) }
            .toObservable()
            .doOnError { it.printStackTrace() }
    }

    private fun mapLaunchesResponse(response: List<LaunchesResponse>): List<LaunchEntity> {
        val launchEntities = arrayListOf<LaunchEntity>()
        response.forEach {
            launchEntities.add(
                LaunchEntity(
                    it.id,
                    it.links.patch.small,
                    it.links.wikipedia,
                    it.links.article,
                    it.links.webcast,
                    it.name,
                    it.date_unix,
                    it.upcoming,
                    it.rocket,
                    it.success,
                    calendarProvider.timeInMillis()
                )
            )
        }
        return launchEntities
    }

    private fun isNotStale(entity: LaunchEntity): Boolean {
        val timeDiff = calendarProvider.timeInMillis() - entity.dt
        return TimeUnit.MILLISECONDS.toSeconds(timeDiff) < 20
    }
}