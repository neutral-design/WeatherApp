package com.example.weatherapp.utils

import android.content.Context
import androidx.core.content.edit
import com.example.weatherapp.model.ParsedWeatherResult
import com.example.weatherapp.model.PlaceSuggestion
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DataStorage {

    private const val PREFS_NAME = "WeatherAppPrefs"
    private const val WEATHER_DATA_KEY = "WeatherData"
    private const val FAVORITES_KEY = "Favorites"

    private val json = Json { encodeDefaults = true }

    // Spara väderdata
    fun saveWeatherData(context: Context, weatherResult: ParsedWeatherResult) {
        val jsonString = Json.encodeToString(weatherResult)
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(WEATHER_DATA_KEY, jsonString)
        }
    }

    // Hämta väderdata
    fun loadWeatherData(context: Context): ParsedWeatherResult? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(WEATHER_DATA_KEY, null)
        return jsonString?.let {
            Json.decodeFromString<ParsedWeatherResult>(it)
        }
    }

    // Rensa sparad väderdata
    fun clearWeatherData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(WEATHER_DATA_KEY)
        }
    }


    // Spara favoriter
    fun saveFavorites(context: Context, favorites: List<PlaceSuggestion>) {
        val jsonString = json.encodeToString(favorites)
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(FAVORITES_KEY, jsonString)
        }
    }

    // Hämta favoriter
    fun loadFavorites(context: Context): List<PlaceSuggestion> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(FAVORITES_KEY, null)
        return jsonString?.let {
            json.decodeFromString<List<PlaceSuggestion>>(it)
        } ?: emptyList()
    }

    // Rensa sparade favoriter
    fun clearFavorites(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(FAVORITES_KEY)
        }
    }
}


