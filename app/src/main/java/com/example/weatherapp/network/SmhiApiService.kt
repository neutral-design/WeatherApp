package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Definiera bas-URL för SMHI:s API
//SMHI-api url
//private const val BASE_URL = "https://opendata-download-metfcst.smhi.se/"


//KTH test API-url
//https://maceo.sth.kth.se/weather/forecast?lonLat=lon/14.333/lat/60.383
private const val BASE_URL = "https://maceo.sth.kth.se/"



//interface SmhiApiService {
//    @GET("api/category/pmp3g/version/2/geotype/point/lon/{lon}/lat/{lat}/data.json")
//    suspend fun getWeatherForecast(
//        @Path("lon") longitude: Float,
//        @Path("lat") latitude: Float
//    ): WeatherApiResponse
//}

//KTH-test api
interface SmhiApiService {

    @GET("weather/forecast")
    suspend fun getWeatherForecast(
        @Query("lonlat") lonLat: String
    ): WeatherApiResponse
}

// Singleton-objekt för att hantera Retrofit-instans
object SmhiApi {
    val retrofitService: SmhiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmhiApiService::class.java)
    }
}
