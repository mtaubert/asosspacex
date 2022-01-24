package com.example.asosspacex.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.asosspacex.db.dao.CompanyInfoDao
import com.example.asosspacex.db.dao.LaunchesDao
import com.example.asosspacex.db.dao.RocketsDao
import com.example.asosspacex.db.entities.CompanyInfoEntity
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity

@Database(entities = [RocketEntity::class, LaunchEntity::class, CompanyInfoEntity::class], version = 1, exportSchema = true)
abstract class SpaceXDatabase: RoomDatabase() {
    abstract fun rocketsDao(): RocketsDao
    abstract fun launchesDao(): LaunchesDao
    abstract fun companyInfoDao(): CompanyInfoDao
}