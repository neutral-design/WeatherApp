package com.example.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openMaps(context: Context, latitude: Double, longitude: Double) {
    val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Om Google Maps inte är installerat, öppna en webbläsare
        val browserIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        context.startActivity(browserIntent)
    }
}
