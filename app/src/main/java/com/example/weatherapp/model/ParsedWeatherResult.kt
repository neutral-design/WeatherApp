package com.example.weatherapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ParsedWeatherResult(
    val weatherData: List<ParsedWeatherData>, // Lista med väderdata
    val coordinates: List<Double>,           // Koordinater för platsen
    val source: String = "SMHI"              // Källa för väderdata
)
