package com.example.weatherapp


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
