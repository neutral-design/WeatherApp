package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.R
import com.example.weatherapp.model.ParsedWeatherData
import com.example.weatherapp.utils.openMaps
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherResult by viewModel.weatherResult.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Visa felmeddelanden
        errorMessage?.let { message ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }


        // Visa laddningsindikator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            weatherResult?.let { result ->
                if (result.weatherData.isNotEmpty()) {
                    // Visa klickbara koordinater
                    if (result.coordinates.isNotEmpty()) {
                        val latitude = result.coordinates[1]
                        val longitude = result.coordinates[0]
                        Text(
                            text = "Location: %.6f, %.6f".format(latitude, longitude),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .clickable {
                                    openMaps(context, latitude, longitude)
                                }
                        )
                    }
                    // Rubrikraden
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Time",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Wind Dir.",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Wind Speed",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Temp",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Clouds",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    // Lista med väderdata
                    LazyColumn {
                        items(result.weatherData) { data ->
                            WeatherItem(data)
                        }
                    }
                } else {
                    Text(
                        text = "No weather data available",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } ?: Text(
                text = "No weather data available",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
        // Ny kolumn för att visa vindriktningen i grader
//        Text(
//            text = "${parsedData.windDirection ?: "N/A"}°",
//            modifier = Modifier.weight(1f),
//            style = MaterialTheme.typography.bodyMedium
//        )
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
    val correctedAngle = (windDirection + 180) % 360 // Lägg till 180° och säkerställ att vinkeln håller sig inom 0–360°

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
                .rotate(correctedAngle.toFloat())
        )
    }
}
