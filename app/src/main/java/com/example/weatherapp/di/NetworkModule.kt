package com.example.weatherapp.di

import com.example.weatherapp.network.SmhiApiService
import com.example.weatherapp.network.SmhiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideSmhiApiService(): SmhiApiService {
        return SmhiApi.retrofitService
    }
}
