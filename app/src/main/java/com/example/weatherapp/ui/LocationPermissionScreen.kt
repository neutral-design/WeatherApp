package com.example.weatherapp.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun LocationPermissionScreen(
    onPermissionResult: () -> Unit,
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current
    val errorMessage by viewModel.errorMessage.observeAsState()
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Uppdatera koordinater om behörigheten beviljas
                viewModel.updateLocationFields()
                Toast.makeText(context, "Location permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Location permission is required to update coordinates.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Navigera tillbaka till MainScreen
            onPermissionResult()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Location permission is required to update coordinates.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Grant Permission")
        }
    }
}


