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
import com.xvega.weatherapp.R
import com.xvega.weatherapp.model.ApiState
import com.xvega.weatherapp.model.WeatherData
import com.xvega.weatherapp.ui.theme.WeatherAppTheme
import com.xvega.weatherapp.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAppContent(
    viewModel: MainViewModel
) {
    WeatherAppTheme {
        val apiStateFlow = viewModel.weatherData.collectAsStateWithLifecycle(ApiState.Empty)
        val apiState by remember { derivedStateOf { apiStateFlow.value } }

        val title = when (apiState) {
            is ApiState.Success -> ((apiState as ApiState.Success).data as WeatherData).location.region
            else -> stringResource(R.string.app_name)
        }
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(title)
                        }
                    )
                }
            )
            { innerPadding ->
                ContentScreen(apiState, Modifier.padding(innerPadding)) {
                    viewModel.refreshData()
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
