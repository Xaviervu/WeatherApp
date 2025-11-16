package com.xvega.weatherapp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xvega.weatherapp.R

@Composable
fun ErrorScreen(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Card(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(horizontal = 16.dp),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.oopsy),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Image(
                    painter = painterResource(id = R.drawable.something_went_wrong),
                    contentDescription = stringResource(R.string.oopsy)
                )
                Button(onClick = onClick) {
                    Text(text = stringResource(R.string.refresh))
                }
            }
        }
    }

}