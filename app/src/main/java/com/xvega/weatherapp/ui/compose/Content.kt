package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xvega.weatherapp.R
import com.xvega.weatherapp.model.ApiState
import com.xvega.weatherapp.model.WeatherData
import com.xvega.weatherapp.ui.theme.WeatherAppTheme
import com.xvega.weatherapp.viewModel.MainViewModel

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Weather : Screen("weather", "Weather", { Icon(Icons.Default.WbSunny, contentDescription = "Weather") })
    object WebView : Screen("webview", "WebView", { Icon(Icons.Default.Language, contentDescription = "WebView") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAppContent(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    WeatherAppTheme {
        val apiStateFlow = viewModel.weatherData.collectAsStateWithLifecycle(ApiState.Empty)
        val apiState by remember { derivedStateOf { apiStateFlow.value } }

        val title = when (currentRoute) {
            Screen.Weather.route -> {
                when (apiState) {
                    is ApiState.Success -> ((apiState as ApiState.Success).data as WeatherData).location.region
                    else -> stringResource(R.string.app_name)
                }
            }
            Screen.WebView.route -> "Weather.com"
            else -> stringResource(R.string.app_name)
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    if (currentRoute == Screen.Weather.route) {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(title)
                            }
                        )
                    }
                },
                bottomBar = {
                    NavigationBar {
                        val items = listOf(Screen.Weather, Screen.WebView)
                        items.forEach { screen ->
                            NavigationBarItem(
                                icon = screen.icon,
                                label = { Text(screen.title) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            )
            { innerPadding ->
                NavHost(navController = navController, startDestination = Screen.WebView.route, modifier = Modifier.padding(innerPadding)) {
                    composable(Screen.Weather.route) {
                        ContentScreen(apiState) {
                            viewModel.refreshData()
                        }
                    }
                    composable(Screen.WebView.route) {
                        WebViewScreen()
                    }
                }
            }
        }
    }
    viewModel.getInitialData()
}

@Composable
private fun ContentScreen(
    state: ApiState,
    modifier: Modifier = Modifier,
    refresh: () -> Unit
) {
    when (state) {
        ApiState.Empty,
        ApiState.Loading -> {
            IndeterminateProgressScreen(
                modifier,
                stringResource(R.string.gathering_weather_data)
            )
        }

        is ApiState.Success -> {
            val weatherData by remember { derivedStateOf { state.data as WeatherData } }
            WeatherScreen(weatherData, modifier)
            state.data.toString()
        }

        is ApiState.Failure -> {
            ErrorScreen(state.e.toString(), modifier, refresh)
        }
    }
}
