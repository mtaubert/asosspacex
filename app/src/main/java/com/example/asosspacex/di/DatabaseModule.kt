package com.example.asosspacex.di

import android.content.Context
import androidx.room.Room
//import com.example.asosspacex.db.CompanyInfoDatabase
//import com.example.asosspacex.db.LaunchesDatabase
//import com.example.asosspacex.db.RocketsDatabase
import com.example.asosspacex.db.SpaceXDatabase
import com.example.asosspacex.db.dao.CompanyInfoDao
import com.example.asosspacex.db.dao.LaunchesDao
import com.example.asosspacex.db.dao.RocketsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideCompanyInfoDatabase(
        @ApplicationContext context: Context
    ): SpaceXDatabase =
        Room.databaseBuilder(
            context,
            SpaceXDatabase::class.java,
            "ASOSSpaceX")
            .build()

    @Provides
    fun provideCompanyInfoDao(db: SpaceXDatabase): CompanyInfoDao {
        return db.companyInfoDao()
    }

    @Provides
    fun provideLaunchesDao(db: SpaceXDatabase): LaunchesDao {
        return db.launchesDao()
    }

    @Provides
    fun provideRocketsDao(db: SpaceXDatabase): RocketsDao {
        return db.rocketsDao()
    }


//    @Provides
//    @Singleton
//    fun provideCompanyInfoDatabase(
//        @ApplicationContext context: Context
//    ): CompanyInfoDatabase =
//        Room.databaseBuilder(
//            context,
//            CompanyInfoDatabase::class.java,
//            "ASOSSpaceX")
//            .build()
//
//    @Provides
//    fun provideCompanyInfoDao(db: CompanyInfoDatabase): CompanyInfoDao {
//        return db.companyInfoDao()
//    }
//
//    @Provides
//    @Singleton
//    fun provideLaunchesDatabase(
//        @ApplicationContext context: Context
//    ): LaunchesDatabase =
//        Room.databaseBuilder(
//            context,
//            LaunchesDatabase::class.java,
//            "ASOSSpaceX")
//            .build()
//
//    @Provides
//    fun provideLaunchesDao(db: LaunchesDatabase): LaunchesDao {
//        return db.launchesDao()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRocketsDatabase(
//        @ApplicationContext context: Context
//    ): RocketsDatabase =
//        Room.databaseBuilder(
//            context,
//            RocketsDatabase::class.java,
//            "ASOSSpaceX")
//            .build()
//
//    @Provides
//    fun provideRocketsDao(db: RocketsDatabase): RocketsDao {
//        return db.rocketsDao()
//    }
}