package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xvega.weatherapp.R
import com.xvega.weatherapp.model.ForecastDay
import com.xvega.weatherapp.utils.imageBitmapFromUrl
import com.xvega.weatherapp.utils.isTimeMoreOrEqualThanCurrentHour

@Composable
fun Hourly(forecastDay: ForecastDay, isToday: Boolean) {
    val hour = if (!isToday) forecastDay.hour else forecastDay.hour.filter {
        isTimeMoreOrEqualThanCurrentHour(it.time)
    }
    val surfaceColor = MaterialTheme.colorScheme.onSurface
    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hour.size) { itemN ->
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val hour = hour[itemN]

                var hourImage: ImageBitmap? by remember { mutableStateOf(null) }
                val context = LocalContext.current

                Text(
                    "${hour.tempC}" + stringResource(R.string.degrees),
                    color = surfaceColor
                )

                LaunchedEffect(forecastDay.day.condition.icon) {
                    hourImage = imageBitmapFromUrl(hour.condition.icon, context)
                }
                hourImage?.let {
                    Image(
                        bitmap = it,
                        contentDescription = hour.condition.text,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    "${hour.chanceOfRain}" + stringResource(R.string.percent),
                    color = surfaceColor
                )
                if (isToday && itemN == 0) {
                    Text(
                        stringResource(R.string.now),
                        color = surfaceColor
                    )
                } else {
                    Text(hour.time.removePrefix(forecastDay.date).trim())
                }
            }

        }
    }
}