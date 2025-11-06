package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
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


@Composable
fun DayCard(
    forecastDay: ForecastDay,
    day: String,
    modifier: Modifier = Modifier,
    isToday: Boolean
) {
    val surfaceColor = MaterialTheme.colorScheme.onSurface
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.padding(top = 16.dp))
            Text(
                day,
                color = surfaceColor
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var avgImage: ImageBitmap? by remember { mutableStateOf(null) }
                    val context = LocalContext.current
                    LaunchedEffect(forecastDay.day.condition.icon) {
                        avgImage = imageBitmapFromUrl(forecastDay.day.condition.icon, context)
                    }
                    avgImage?.let {
                        Image(
                            bitmap = it,
                            contentDescription = forecastDay.day.condition.text,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = forecastDay.day.maxtempC.toString() + stringResource(R.string.degrees),
                            color = surfaceColor
                        )
                        Text(
                            text = forecastDay.day.mintempC.toString() + stringResource(R.string.degrees),
                        )
                    }
                }

                Text(
                    text = forecastDay.day.avghumidity.toString() + stringResource(R.string.percent),
                    color = surfaceColor
                )

            }

            Hourly(forecastDay, isToday)
        }
    }
}