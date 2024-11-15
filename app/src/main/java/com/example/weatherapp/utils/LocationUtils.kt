package com.example.weatherapp.utils

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationUtils @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        Log.d("LocationUtils", "getCurrentLocation")
        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                Log.d("LocationUtils", "$location")
                cont.resume(location)
            }.addOnFailureListener {
                cont.resume(null)
            }
        }
    }
}
