package com.example.pokedexapp.util

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DominantColorExtractor {

    suspend fun extractDominantColor(
        imageUrl: String?,
        context: Context,
        fallbackColor: Color = Color.Gray
    ): Color {
        return withContext(Dispatchers.IO) {
            try {
                if (imageUrl.isNullOrBlank()) return@withContext fallbackColor

                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()

                val imageLoader = ImageLoader(context)
                val result = imageLoader.execute(request)

                if (result is SuccessResult) {
                    val drawable = result.drawable
                    val bitmap = drawable.toBitmap()
                    val palette = Palette.from(bitmap).generate()

                    val colorInt = palette.dominantSwatch?.rgb
                        ?: palette.vibrantSwatch?.rgb
                        ?: palette.lightVibrantSwatch?.rgb
                        ?: palette.darkVibrantSwatch?.rgb
                        ?: palette.mutedSwatch?.rgb
                        ?: fallbackColor.value.toInt()

                    Color(colorInt)
                } else {
                    fallbackColor
                }
            } catch (e: Exception) {
                fallbackColor
            }
        }
    }
}

@Composable
fun rememberDominantColor(
    imageUrl: String?,
    fallbackColor: Color = Color.Gray
): State<Color> {
    val context = LocalContext.current
    val dominantColor = remember { mutableStateOf(fallbackColor) }

    LaunchedEffect(imageUrl) {
        val extractedColor = DominantColorExtractor.extractDominantColor(
            imageUrl = imageUrl,
            context = context,
            fallbackColor = fallbackColor
        )
        dominantColor.value = extractedColor
    }

    return dominantColor
}