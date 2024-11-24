package com.example.weatherapp.network

import com.example.weatherapp.model.PlaceSuggestion
import com.example.weatherapp.model.WeatherApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Ny bas-URL för sök-API
//private const val SEARCH_BASE_URL = "https://www.smhi.se/"

// Interface för sökfunktionen
interface LocationSearchApiService {
    @GET("wpt-a/backend_solr/autocomplete/search/{place}")
    suspend fun searchPlace(
        @Path("place") place: String
    ): List<PlaceSuggestion>
}

// Singleton-objekt för att hantera sök-API
//object LocationSearchApi {
//    val retrofitService: LocationSearchApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(SEARCH_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(LocationSearchApiService::class.java)
//    }
//}
