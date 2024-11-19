package com.example.weatherapp.ui

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.weatherapp.viewmodel.WeatherViewModel

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.weatherapp.navigation.WeatherTab


@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    navController: androidx.navigation.NavController,
    onTabChange: (WeatherTab) -> Unit // Ny parameter
) {
    val latitudeState = viewModel.latitude.observeAsState("")
    val longitudeState = viewModel.longitude.observeAsState("")

    val latitude = latitudeState.value
    val longitude = longitudeState.value

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = latitude,
            onValueChange = { newValue ->
                if (newValue.toFloatOrNull() != null || newValue.isEmpty()) {
                    viewModel.updateLatitude(newValue)
                }
            },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = longitude,
            onValueChange = { newValue ->
                if (newValue.toFloatOrNull() != null || newValue.isEmpty()) {
                    viewModel.updateLongitude(newValue)
                }
            },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Behörighet finns, uppdatera koordinater direkt
                    viewModel.updateLocationFields()
                } else {
                    // Behörighet saknas, navigera till LocationPermissionScreen
                    navController.navigate("location_permission")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Coordinates")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                val lat = latitude.toFloatOrNull()
                val lon = longitude.toFloatOrNull()

                if (lat == null || lon == null) {
                    // Visa ett felmeddelande eller avbryt anropet
                    println("Invalid coordinates")
                    // Visa Toast för ogiltig inmatning
                    Toast.makeText(context, "Invalid coordinates. Please enter valid numbers.", Toast.LENGTH_SHORT).show()
                } else {
                    // Hämta väderdata och navigera till WeatherScreen
                    viewModel.fetchWeather(lat, lon)
                    onTabChange(WeatherTab.Weather)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Weather")
        }

    }
}
