package com.xvega.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.xvega.weatherapp.ui.compose.SetAppContent
import com.xvega.weatherapp.ui.theme.WeatherAppTheme
import com.xvega.weatherapp.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherAppTheme {
                SetAppContent(
                    viewModel
                )
            }
        }
    }
}