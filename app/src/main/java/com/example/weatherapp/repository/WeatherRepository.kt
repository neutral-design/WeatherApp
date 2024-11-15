package com.example.weatherapp.repository

import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.model.TimeSeries
import com.example.weatherapp.network.SmhiApi
import com.example.weatherapp.network.SmhiApiService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


//TODO: BYgg om till att använda Hilt för DI


class WeatherRepository @Inject constructor(
    private val smhiApiService: SmhiApiService
) {
    suspend fun fetchWeather(longitude: Float, latitude: Float): Result<List<ParsedWeatherData>> {
        return try {
            // Ensure coordinates are valid before making API request
            if (!areCoordinatesValid(longitude, latitude)) {
                return Result.failure(Exception("Coordinates out of bounds"))
            }

            //Annorlunda API-call pga KTH-testserver
            val response = SmhiApi.retrofitService.getWeatherForecast(
                lonLat = "lon/$longitude/lat/$latitude"

            )
            //Korrekt API-call till SMHIs server
            //val response = SmhiApi.retrofitService.getWeatherForecast(longitude, latitude)

            val parsedData = response.timeSeries?.map { timeSeries ->
                parseTimeSeries(timeSeries)
            } ?: emptyList()

            Result.success(parsedData)
        } catch (e: Exception) {
            Result.failure(e)
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
    private fun areCoordinatesValid(longitude: Float, latitude: Float): Boolean {
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
}
