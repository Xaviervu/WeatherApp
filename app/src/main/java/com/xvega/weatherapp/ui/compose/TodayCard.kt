package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import com.xvega.weatherapp.R
import com.xvega.weatherapp.model.Current
import com.xvega.weatherapp.model.ForecastDay
import com.xvega.weatherapp.utils.imageBitmapFromUrl

@Composable
fun TodayCard(
    current: Current,
    forecastDay: ForecastDay,
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            var avgImage: ImageBitmap? by remember { mutableStateOf(null) }
            val context = LocalContext.current
            LaunchedEffect(current.condition.icon) {
                avgImage = imageBitmapFromUrl(current.condition.icon, context)
            }
            avgImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = current.condition.text,
                    modifier = Modifier.size(52.dp)
                )
            }
            Text(
                current.condition.text,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        val deg = stringResource(R.string.degrees)
        Text(
            text = current.tempC.toString() + deg,
            fontSize = 100.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 20.dp)
        )
        Text(
            text = stringResource(R.string.feels_like) + " " + current.feelslikeC.toString() + deg,
            color = MaterialTheme.colorScheme.onSurface
        )
        val highLow =
            stringResource(R.string.high) + " " + forecastDay.day.maxtempC + deg + " " +
                    stringResource(R.string.low) + " " + forecastDay.day.mintempC + deg

        Text(
            text = highLow,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    Spacer(Modifier.padding(20.dp))
}