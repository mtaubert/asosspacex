package com.example.asosspacex.db.dao

import androidx.room.*
import com.example.asosspacex.db.entities.LaunchEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface LaunchesDao {
    @Query("SELECT * FROM launches")
    fun getAll(): Single<List<LaunchEntity>>

    @Query("SELECT * FROM launches WHERE id LIKE :id LIMIT 1")
    fun getById(id: String): Single<LaunchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaunch(launchEntity: LaunchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(launches: List<LaunchEntity>)

    @Delete
    fun deleteLaunch(launchEntity: LaunchEntity)
}