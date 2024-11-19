package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.ui.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapp.navigation.WeatherTabsNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTabsNavHost() // Startar navigationen
        }
    }
}

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    private val locationPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                Log.d("MainActivity", "Location permission granted")
//                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show()
//                weatherViewModel?.updateLocationFields()
//            } else {
//                Log.d("MainActivity", "Location permission denied")
//                Toast.makeText(
//                    this,
//                    "Location permission is required to update coordinates.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    private var weatherViewModel: WeatherViewModel? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            val viewModel: WeatherViewModel = hiltViewModel()
//            weatherViewModel = viewModel
//            MainContent(viewModel)
//        }
//    }
//
//    @Composable
//    private fun MainContent(viewModel: WeatherViewModel) {
//        // Observera latitude och longitude från ViewModel
//        val latitudeState = viewModel.latitude.observeAsState(0f)
//        val longitudeState = viewModel.longitude.observeAsState(0f)
//
//        // Hämta värdena från State
//        val latitude = latitudeState.value
//        val longitude = longitudeState.value
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TextField(
//                value = latitude.toString(),
//                onValueChange = {}, // Gör den bara läsbar
//                label = { Text("Latitude") },
//                readOnly = true,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            TextField(
//                value = longitude.toString(),
//                onValueChange = {}, // Gör den bara läsbar
//                label = { Text("Longitude") },
//                readOnly = true,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { checkLocationPermission { viewModel.updateLocationFields() } },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Update Coordinates")
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { viewModel.fetchWeather(latitude, longitude) },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Fetch Weather")
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            WeatherScreen(viewModel = viewModel) // Skicka ViewModel till WeatherScreen
//        }
//    }
//
//
//
//    private fun checkLocationPermission(onPermissionGranted: () -> Unit) {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        } else {
//            onPermissionGranted()
//        }
//    }
//}

