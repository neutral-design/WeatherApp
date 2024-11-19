package com.example.weatherapp.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.MainScreen
import com.example.weatherapp.ui.SearchByNameScreen
import com.example.weatherapp.ui.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel

sealed class WeatherTab(val title: String, val route: String) {
    object Main : WeatherTab("Home", "main")
    object Search : WeatherTab("Search", "search_by_name")
    object Weather : WeatherTab("Weather", "weather")
}

val weatherTabs = listOf(
    WeatherTab.Main,
    WeatherTab.Search,
    WeatherTab.Weather
)

@Composable
fun WeatherTabsNavHost(viewModel: WeatherViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    // Spara flikens route istället för WeatherTab-objektet
    var selectedTabRoute by rememberSaveable { mutableStateOf(WeatherTab.Main.route) }

    val selectedTab = weatherTabs.find { it.route == selectedTabRoute } ?: WeatherTab.Main

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = weatherTabs.indexOf(selectedTab),
                modifier = Modifier.fillMaxWidth()
            ) {
                weatherTabs.forEach { tab ->
                    Tab(
                        selected = tab.route == selectedTabRoute,
                        onClick = {
                            selectedTabRoute = tab.route
                            navController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        text = { Text(tab.title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = WeatherTab.Main.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(WeatherTab.Main.route) {
                MainScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onTabChange = { tab ->
                        selectedTabRoute = tab.route
                        navController.navigate(tab.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(WeatherTab.Search.route) {
                SearchByNameScreen(
                    onPlaceSelected = { lat, lon ->
                        viewModel.updateLatitude(lat.toString())
                        viewModel.updateLongitude(lon.toString())
                        viewModel.fetchWeather(lat, lon) // Hämta väderdata för vald plats
                        selectedTabRoute = WeatherTab.Weather.route
                        navController.navigate(WeatherTab.Weather.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(WeatherTab.Weather.route) {
                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}

//
//@Composable
//fun WeatherTabsNavHost(viewModel: WeatherViewModel = hiltViewModel()) {
//    val navController = rememberNavController()
//    var selectedTab by rememberSaveable { mutableStateOf<WeatherTab>(WeatherTab.Main) }
//
//    Scaffold(
//        topBar = {
//            TabRow(
//                selectedTabIndex = weatherTabs.indexOf(selectedTab),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                weatherTabs.forEach { tab ->
//                    Tab(
//                        selected = tab == selectedTab,
//                        onClick = {
//                            if (tab != selectedTab) {
//                                selectedTab = tab
//                                navController.navigate(tab.route) {
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            }
//                        },
//                        text = { Text(tab.title) }
//                    )
//                }
//            }
//        }
//    ) { padding ->
//        NavHost(
//            navController = navController,
//            startDestination = WeatherTab.Main.route,
//            modifier = Modifier.padding(padding)
//        ) {
//            composable(WeatherTab.Main.route) {
//                MainScreen(
//                    viewModel = viewModel,
//                    navController = navController,
//                    onTabChange = { tab -> // Skicka logik för att byta tab
//                        selectedTab = tab
//                        navController.navigate(tab.route) {
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
//            }
//            composable(WeatherTab.Search.route) {
//                SearchByNameScreen(
//                    onPlaceSelected = { lat, lon ->
//                        viewModel.updateLatitude(lat.toString())
//                        viewModel.updateLongitude(lon.toString())
//                        viewModel.fetchWeather(lat, lon) // Hämta väderdata för vald plats
//                        selectedTab = WeatherTab.Weather
//                        navController.navigate(WeatherTab.Weather.route) {
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
//                    viewModel = viewModel
//                )
//            }
//            composable(WeatherTab.Weather.route) {
//                WeatherScreen(viewModel = viewModel)
//            }
//        }
//    }
//}




//@Composable
//fun WeatherAppNavHost(viewModel: WeatherViewModel = hiltViewModel()) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "main") {
//        composable("main") {
//            MainScreen(
//                viewModel = viewModel,
//                navController = navController
//            )
//        }
//        composable("location_permission") {
//            LocationPermissionScreen(
//                onPermissionResult = { navController.popBackStack() },
//                viewModel = viewModel
//            )
//        }
//        composable("weather") {
//            WeatherScreen(viewModel = viewModel)
//        }
//        composable("search_by_name") {
//            SearchByNameScreen(
//                onPlaceSelected = { lon, lat ->
//                    viewModel.fetchWeather(lon, lat)
//                    navController.navigate("weather")
//                },
//                viewModel = viewModel
//            )
//        }
//    }
//}



