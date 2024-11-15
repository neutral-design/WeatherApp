package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.ui.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var locationPermissionGranted = false

    // Launcher för att be om platsbehörighet
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            locationPermissionGranted = isGranted // Uppdatera om behörigheten är beviljad
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Hämta ViewModel från Hilt (en instans som lever genom hela aktivitetslivscykeln)
            val weatherViewModel: WeatherViewModel = hiltViewModel()

            // Skicka ViewModel till WeatherScreen
            WeatherScreen(
                viewModel = weatherViewModel,
                onUpdateLocationClick = { checkLocationPermission(weatherViewModel) }
            )
        }
    }

    // Kolla om behörighet behövs och be om den när användaren trycker på "Update Coordinates"
    private fun checkLocationPermission(viewModel: WeatherViewModel) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Om behörigheten är redan beviljad, hämta koordinater
            viewModel.updateLocationFields()
        }
    }

}

