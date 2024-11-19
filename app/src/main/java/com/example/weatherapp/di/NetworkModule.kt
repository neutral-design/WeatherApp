package com.example.weatherapp.di

import com.example.weatherapp.network.LocationSearchApiService
import com.example.weatherapp.network.SmhiApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val SEARCH_BASE_URL = "https://www.smhi.se/"

    private const val WEATHER_BASE_URL = "https://opendata-download-metfcst.smhi.se/"
    //private const val WEATHER_BASE_URL = "https://maceo.sth.kth.se/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideSmhiApiService(retrofitBuilder: Retrofit.Builder): SmhiApiService {
        return retrofitBuilder
            .baseUrl(WEATHER_BASE_URL)
            .build()
            .create(SmhiApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLocationSearchApiService(retrofitBuilder: Retrofit.Builder): LocationSearchApiService {
        return retrofitBuilder
            .baseUrl(SEARCH_BASE_URL)
            .build()
            .create(LocationSearchApiService::class.java)
    }
}



