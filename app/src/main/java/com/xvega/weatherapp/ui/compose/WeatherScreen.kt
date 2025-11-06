package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.xvega.weatherapp.R
import com.xvega.weatherapp.model.Condition
import com.xvega.weatherapp.model.Day
import com.xvega.weatherapp.model.Forecast
import com.xvega.weatherapp.model.ForecastDay
import com.xvega.weatherapp.model.Hour
import com.xvega.weatherapp.model.WeatherData
import com.xvega.weatherapp.ui.theme.WeatherAppTheme
import com.xvega.weatherapp.utils.getWeekDayByDate

@Composable
fun WeatherScreen(
    innerPadding: PaddingValues,
    weatherData: WeatherData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val startDays = 1
        LazyColumn {

            items(weatherData.forecast.forecastDayList.size + startDays) { itemN ->
                if (itemN == 0) {
                    TodayCard(weatherData.current, weatherData.forecast.forecastDayList.first())
                } else {
                    val foreCastDay = weatherData.forecast.forecastDayList[itemN - startDays]
                    val day =
                        if (itemN == startDays) {
                            stringResource(R.string.today)
                        } else getWeekDayByDate(foreCastDay.date, LocalContext.current)
                            ?: foreCastDay.date
                    DayCard(
                        forecastDay = foreCastDay,
                        day = day,
                        isToday = itemN == startDays
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WeatherAppTheme {
        val iconUrl = "//cdn.weatherapi.com/weather/64x64/day/176.png"
        val conditionData = Condition().apply {
            icon = iconUrl
            text = "Partly Cloudy"
        }
        val weatherData = WeatherData().apply {
            current.apply {
                tempC = 22.0
                feelslikeC = 21.0
            }
            forecast = Forecast().apply {
                forecastDayList.add(
                    ForecastDay().apply {
                        date = "2025-11-06"
                        day = Day()
                        day.apply {
                            avgtempC = 25.0
                            maxtempC = 31.0
                            mintempC = 24.0
                            avghumidity = 17
                            condition = conditionData
                        }
                        hour = arrayListOf(Hour().apply {
                            tempC = 22.5
                            condition = conditionData
                            time = "2025-11-06 11:11"
                        }
                        )
                    }
                )
            }
        }
        WeatherScreen(
            PaddingValues.Zero,
            weatherData
        )
    }
}