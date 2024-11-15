package com.example.weatherapp.model

data class WeatherApiResponse(
    val approvedTime: String?,
    val referenceTime: String?,
    val geometry: Geometry?,
    val timeSeries: List<TimeSeries>?
)

