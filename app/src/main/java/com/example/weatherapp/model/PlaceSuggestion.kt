package com.example.weatherapp.model

data class PlaceSuggestion(
    val geonameid: Int,
    val place: String,
    val population: Int,
    val lon: Double,
    val lat: Double,
    val type: List<String>,
    val country: String
)
