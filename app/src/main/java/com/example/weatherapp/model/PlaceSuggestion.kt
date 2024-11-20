package com.example.weatherapp.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceSuggestion(
    val geonameid: Int,
    val place: String,
    val population: Int,
    val lon: Double,
    val lat: Double,
    val type: List<String>,
    val country: String,
    val municipality: String,  // Nytt fält
    val county: String,        // Nytt fält
    val district: String       // Nytt fält
)
