package com.example.asosspacex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
       Retrofit.Builder()
           .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
           .addConverterFactory(GsonConverterFactory.create())
           .client(getOkHttpLoggingInterceptorClient())
           .baseUrl("https://api.spacexdata.com")
           .build()

    private fun getOkHttpLoggingInterceptorClient(): OkHttpClient {
        val logginfInterceptor = HttpLoggingInterceptor()
        logginfInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(logginfInterceptor).build()
    }
}