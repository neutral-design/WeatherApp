package com.example.weatherapp.model

data class Parameter(
    val name: String,
    val levelType: String,
    val level: Int,
    val values: List<Float>
)
