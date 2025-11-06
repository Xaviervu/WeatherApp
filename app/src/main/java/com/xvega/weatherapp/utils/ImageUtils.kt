package com.xvega.weatherapp.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun imageBitmapFromUrl(imageUrlRaw: String, context: Context): ImageBitmap? = withContext(
    Dispatchers.IO
) {
    val imageUrl = "https:$imageUrlRaw"
    val imageRequest =
        ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    val result = Coil.imageLoader(context).execute(imageRequest)
    val imageBitmap = if (result is SuccessResult) {
        (result.drawable as? BitmapDrawable)?.bitmap?.asImageBitmap()
    } else {
        null
    }
    imageBitmap
}