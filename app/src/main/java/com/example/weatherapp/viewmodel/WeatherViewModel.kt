package com.example.weatherapp.viewmodel

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationUtils: LocationUtils
) : ViewModel() {

    private val _locationPermissionGranted = MutableLiveData(false)
    val locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted

    private val _weatherData = MutableLiveData<List<ParsedWeatherData>>(emptyList())
    val weatherData: LiveData<List<ParsedWeatherData>> = _weatherData

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _latitude = MutableLiveData<Float>(60.383f) // Sätt defaultvärde för latitude
    val latitude: LiveData<Float> = _latitude

    private val _longitude = MutableLiveData<Float>(14.333f) // Sätt defaultvärde för longitude
    val longitude: LiveData<Float> = _longitude


    fun setLocationPermissionGranted(granted: Boolean) {
        _locationPermissionGranted.value = granted
    }

    fun fetchWeather(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.fetchWeather(longitude, latitude)
            result.onSuccess { data ->
                _weatherData.value = data
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }

    fun fetchLocationAndWeather() {
        viewModelScope.launch {
            _isLoading.value = true
            val location = locationUtils.getCurrentLocation()
            if (location != null) {
                val lat = location.latitude.toFloat()
                val lon = location.longitude.toFloat()
                _latitude.value = lat
                _longitude.value = lon
                fetchWeather(lat, lon)
            } else {
                _errorMessage.value = "Unable to retrieve location."
            }
            _isLoading.value = false
        }
    }

    fun updateLocationFields() {
        viewModelScope.launch {
            val location = locationUtils.getCurrentLocation()
            if (location != null) {
                _latitude.value = location.latitude.toFloat()
                _longitude.value = location.longitude.toFloat()
            } else {
                _errorMessage.value = "Unable to retrieve location."
            }
        }
    }

    // Uppdatera felmeddelanden
    fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
