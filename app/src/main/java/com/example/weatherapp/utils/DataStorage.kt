package com.example.weatherapp.utils

import android.content.Context
import androidx.core.content.edit
import com.example.weatherapp.model.ParsedWeatherResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DataStorage {

    private const val PREFS_NAME = "WeatherAppPrefs"
    private const val WEATHER_DATA_KEY = "WeatherData"
    private const val FAVOURITES_KEY = "Favourites"

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
    fun saveFavourites(context: Context, favourites: List<String>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putStringSet(FAVOURITES_KEY, favourites.toSet())
        }
    }

    // Hämta favoriter
    fun loadFavourites(context: Context): List<String> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favouritesSet = sharedPreferences.getStringSet(FAVOURITES_KEY, emptySet())
        return favouritesSet?.toList() ?: emptyList()
    }

    // Rensa sparade favoriter
    fun clearFavourites(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(FAVOURITES_KEY)
        }
    }
}


