package com.example.weatherapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.model.ParsedWeatherResult
import com.example.weatherapp.model.PlaceSuggestion
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.DataStorage
import com.example.weatherapp.utils.LocationUtils
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @ApplicationContext private val context: Context, // Injicera Context
    private val repository: WeatherRepository,
    private val locationUtils: LocationUtils
) : ViewModel() {

    private val _locationPermissionGranted = MutableLiveData(false)
    val locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted

    private val _favourites = MutableLiveData<List<String>>(emptyList())
    val favourites: LiveData<List<String>> = _favourites

    // Exponera hela ParsedWeatherResult
    private val _weatherResult = MutableLiveData<ParsedWeatherResult?>()
    val weatherResult: LiveData<ParsedWeatherResult?> = _weatherResult

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _latitude = MutableLiveData("60.383") // Default som String
    val latitude: LiveData<String> = _latitude

    private val _longitude = MutableLiveData("14.333") // Default som String
    val longitude: LiveData<String> = _longitude

    private val _placeSuggestions = MutableLiveData<List<PlaceSuggestion>>(emptyList())
    val placeSuggestions: LiveData<List<PlaceSuggestion>> = _placeSuggestions

    init {
        loadSavedWeatherData()
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _locationPermissionGranted.value = granted
    }

    fun fetchWeather(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            if (isNetworkAvailable()) {
                _isLoading.value = true
                _errorMessage.value = null
                try {
                    val result = repository.fetchWeather(latitude, longitude)
                    _weatherResult.value = result
                    _errorMessage.value = null // Töm eventuella gamla fel
                } catch (e: Exception) {
                    _weatherResult.value = null // Rensa väderresultat
                    _errorMessage.value = e.message
                } finally {
                    _isLoading.value = false
                }
            } else {
                _errorMessage.value = "No internet connection. Showing saved data."
                loadSavedWeatherData() // Visa sparad data om internet saknas
            }
        }
    }

    fun updateLatitude(newValue: String) {
        _latitude.value = newValue
    }

    fun updateLongitude(newValue: String) {
        _longitude.value = newValue
    }

    fun updateLocationFields() {
        viewModelScope.launch {
            val location = locationUtils.getCurrentLocation()
            if (location != null) {
                _latitude.value = location.latitude.toString()
                _longitude.value = location.longitude.toString()
            } else {
                _errorMessage.value = "Unable to retrieve location."
            }
        }
    }

    // Uppdatera felmeddelanden
    fun updateErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    // Funktion för att söka efter platsnamn
    fun searchPlace(query: String) {
        viewModelScope.launch {
            if (isNetworkAvailable()) {
                _isLoading.value = true
                try {
                    val results = repository.searchPlace(query)
                    _placeSuggestions.value = results
                } catch (e: Exception) {
                    _errorMessage.value = e.message
                } finally {
                    _isLoading.value = false
                }
            } else {
                _errorMessage.value = "No internet connection. Try again later."
            }
        }
    }

    // Hämta sparad väderdata från lagring via Repository
    private fun loadSavedWeatherData() {
        val savedData = repository.loadWeatherData()
        if (savedData != null) {
            _weatherResult.value = savedData
        }
    }

    // Kontrollera nätverksanslutning
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}
