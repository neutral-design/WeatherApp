package com.example.weatherapp.repository

import android.content.Context
import android.util.Log
import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.model.ParsedWeatherResult
import com.example.weatherapp.model.PlaceSuggestion
import com.example.weatherapp.model.TimeSeries
import com.example.weatherapp.network.LocationSearchApiService
import com.example.weatherapp.network.SmhiApiService
import com.example.weatherapp.utils.DataStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject




class WeatherRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationSearchApi: LocationSearchApiService,
    private val smhiApiService: SmhiApiService
) {
    // Sök efter plats
    suspend fun searchPlace(query: String): List<PlaceSuggestion> {
        return locationSearchApi.searchPlace(query)
    }

    // Hämta väderdata från API eller från lagring om något går fel
    suspend fun fetchWeather(latitude: Float, longitude: Float): ParsedWeatherResult {
        // Validera koordinater
        if (!areCoordinatesValid(latitude, longitude)) {
            throw IllegalArgumentException("Coordinates out of bounds")
        }

        return try {
            // Annorlunda API-call pga KTH-testserver
//            val response = smhiApiService.getWeatherForecast(
//                lonLat = "lon/$longitude/lat/$latitude"
//            )
            // Korrekt API-call till SMHIs server
            val response = smhiApiService.getWeatherForecast(latitude, longitude)

            Log.d("WeatherRepository", "Fetched Weather from server: $response")

            // Transformera API-respons till ParsedWeatherData
            val parsedData = response.timeSeries?.map { timeSeries ->
                parseTimeSeries(timeSeries)
            } ?: emptyList()

            val result = ParsedWeatherResult(
                weatherData = parsedData,
                coordinates = response.geometry?.coordinates?.flatten() ?: emptyList()
            )

            // Spara resultatet till lokal lagring
            saveWeatherData(result)

            result
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather data", e)

            // Hämta data från lokal lagring om API-anropet misslyckas
            loadWeatherData() ?: ParsedWeatherResult(
                weatherData = emptyList(),
                coordinates = listOf(latitude.toDouble(), longitude.toDouble())
            )
        }
    }


    /**
     * Checks if the provided latitude and longitude are within an approximate bounding box
     * for the valid geographic area. This function assumes a rectangular region based on
     * the min and max latitude and longitude values and does not take into account the
     * irregular polygonal shape of the actual area.
     * https://opendata.smhi.se/apidocs/metfcst/geographic_area.html
     *
     * Note: This is an approximation and may return true for some points that are outside
     * the precise polygon boundaries, especially near the corners of the valid area.
     * For stricter validation, consider implementing a point-in-polygon check.
     */
    private fun areCoordinatesValid(latitude: Float, longitude: Float): Boolean {
        val isLatValid = latitude in 52.500440..70.740996
        val isLonValid = longitude in -8.541278..37.848053
        return isLatValid && isLonValid
    }

    private fun parseTimeSeries(timeSeries: TimeSeries): ParsedWeatherData {
        val temperature = timeSeries.parameters.find { it.name == "t" }?.values?.firstOrNull()
        val cloudCoverage = timeSeries.parameters.find { it.name == "tcc_mean" }?.values?.firstOrNull()?.toInt()
        val windSpeed = timeSeries.parameters.find { it.name == "ws" }?.values?.firstOrNull()
        val windDirection = timeSeries.parameters.find { it.name == "wd" }?.values?.firstOrNull()?.toInt()
        val weatherSymbol = timeSeries.parameters.find { it.name == "wsymb2" }?.values?.firstOrNull()?.toInt()
        val formattedTime = formatValidTime(timeSeries.validTime)

        return ParsedWeatherData(
            temperature = temperature,
            cloudCoverage = cloudCoverage,
            windSpeed = windSpeed,
            windDirection = windDirection,
            weatherSymbol = weatherSymbol,
            formattedTime = formattedTime
        )
    }

    private fun formatValidTime(validTime: String?): String {
        return validTime?.let {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                val date = inputFormat.parse(it)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                "N/A"
            }
        } ?: "N/A"
    }

    fun saveWeatherData(weatherResult: ParsedWeatherResult) {
        DataStorage.saveWeatherData(context, weatherResult)
    }

    fun loadWeatherData(): ParsedWeatherResult? {
        return DataStorage.loadWeatherData(context)
    }
}
