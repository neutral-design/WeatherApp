// ParsedWeatherData.kt
package com.example.weatherapp.model

data class ParsedWeatherData(
    val temperature: Float?,
    val cloudCoverage: Int?,
    val windSpeed: Float?,
    val windDirection: Int?,
    val weatherSymbol: Int?,
    val formattedTime: String
)
