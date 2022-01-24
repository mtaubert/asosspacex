package com.example.asosspacex.db.dao

import androidx.room.*
import com.example.asosspacex.db.entities.RocketEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface RocketsDao {
    @Query("SELECT * FROM rockets")
    fun getAll(): Single<List<RocketEntity>>

    @Query("SELECT * FROM rockets WHERE id LIKE :id LIMIT 1")
    fun getById(id: String): Single<RocketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRocket(rocketEntity: RocketEntity)

    @Delete
    fun deleteRocket(rocketEntity: RocketEntity)
}