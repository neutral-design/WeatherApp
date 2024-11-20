package com.example.weatherapp.repository

import android.content.Context
import com.example.weatherapp.model.PlaceSuggestion
import com.example.weatherapp.utils.DataStorage
import javax.inject.Inject

class FavoriteRepository @Inject constructor() {

    fun getFavorites(context: Context): List<PlaceSuggestion> {
        return DataStorage.loadFavorites(context)
    }

    fun addFavorite(context: Context, placeSuggestion: PlaceSuggestion) {
        // Hämta befintliga favoriter
        val favorites = getFavorites(context).toMutableList()

        // Kontrollera om platsen redan finns
        if (favorites.none { it.geonameid == placeSuggestion.geonameid }) {
            favorites.add(placeSuggestion)

            // Spara tillbaka den uppdaterade listan
            DataStorage.saveFavorites(context, favorites)
        }
    }

    fun removeFavorite(context: Context, geonameid: Int) {
        // Filtrera bort den specifika platsen
        val updatedFavorites = getFavorites(context).filter { it.geonameid != geonameid }

        // Spara tillbaka den uppdaterade listan
        DataStorage.saveFavorites(context, updatedFavorites)
    }
}
