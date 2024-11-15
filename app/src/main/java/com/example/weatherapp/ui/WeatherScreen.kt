package com.example.weatherapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.R
import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onUpdateLocationClick: () -> Unit
){
    val errorMessage by viewModel.errorMessage.observeAsState()
    val weatherData by viewModel.weatherData.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)


    val latitude by viewModel.latitude.observeAsState()
    val longitude by viewModel.longitude.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Lottie sunny animation
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sunny))
        LottieAnimation(
            composition,
            modifier = Modifier.size(40.dp),
            iterations = LottieConstants.IterateForever
        )

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        TextField(
            value = latitude?.toString() ?: "",
            onValueChange = { /* Låt detta vara tomt för att endast visa värdet */ },
            label = { Text("Latitude") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = longitude?.toString() ?: "",
            onValueChange = { /* Låt detta vara tomt för att endast visa värdet */ },
            label = { Text("Longitude") },
            readOnly = true
        )
        // Knappar
        Button(
            onClick = { viewModel.updateLocationFields() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Coordinates")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.fetchWeather(latitude ?: 0f, longitude ?: 0f) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Weather")
        }
        Spacer(modifier = Modifier.height(8.dp))


        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (weatherData.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Date", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)

                Text(text = "Wind direction", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Text(text = "Wind speed", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Text(text = "Temperature", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Text(text = "Cloud coverage", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            }
            LazyColumn {
                items(weatherData) { parsedData ->
                    WeatherItem(parsedData)
                }
            }
        }
    }
}


@Composable
fun WeatherItem(parsedData: ParsedWeatherData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = parsedData.formattedTime, modifier = Modifier.weight(1f))
        WindDirectionIcon(parsedData.windDirection ?: 0, Modifier.weight(1f))
        Text(text = "${parsedData.windSpeed ?: "N/A"} m/s", modifier = Modifier.weight(1f))

        Text(text = "${parsedData.temperature ?: "N/A"}°C", modifier = Modifier.weight(1f))
        CloudCoverageIcon(cloudCoverage = parsedData.cloudCoverage ?: 0, Modifier.weight(1f))
    }
}



@Composable
fun CloudCoverageIcon(cloudCoverage: Int, modifier: Modifier = Modifier) {
    val sunColor = Color.Yellow
    val cloudColor = Color.Gray.copy(alpha = (cloudCoverage / 8f).coerceIn(0f, 1f))

    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.WbSunny,
            contentDescription = "Sun",
            tint = sunColor,
            modifier = Modifier.size(40.dp)
        )
        Icon(
            imageVector = Icons.Filled.Cloud,
            contentDescription = "Cloud coverage",
            tint = cloudColor,
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer(alpha = (cloudCoverage / 8f).coerceIn(0f, 1f))
        )
    }
}

@Composable
fun WindDirectionIcon(windDirection: Int, modifier: Modifier = Modifier) {
    val arrowColor = Color(0xFF2196F3)
    val rotationAngle = windDirection.toFloat()

    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Navigation,
            contentDescription = "Wind Direction",
            tint = arrowColor,
            modifier = Modifier
                .size(30.dp)
                .rotate(rotationAngle)
        )
    }
}