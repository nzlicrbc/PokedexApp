package com.example.pokedexapp.util

import com.example.pokedexapp.R
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

sealed class DominantColorState {
    object Loading : DominantColorState()
    data class Success(val color: Color) : DominantColorState()
    data class Error(val message: String) : DominantColorState()
}

object DominantColorExtractor {

    suspend fun extractDominantColor(
        imageUrl: String?,
        context: Context
    ): DominantColorState {
        return withContext(Dispatchers.IO) {
            try {
                if (imageUrl.isNullOrBlank()) {
                    return@withContext DominantColorState.Error(context.getString(R.string.error_invalid_image_url))
                }

                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(Constants.IMAGE_HARDWARE_ACCELERATION_ENABLED)
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

                    if (colorInt != null) {
                        DominantColorState.Success(Color(colorInt))
                    } else {
                        DominantColorState.Error(context.getString(R.string.error_extract_color_failed))
                    }
                } else {
                    DominantColorState.Error(context.getString(R.string.error_image_load_failed))
                }
            } catch (e: Exception) {
                DominantColorState.Error(e.message ?: context.getString(R.string.error_unknown))
            }
        }
    }
}

@Composable
fun rememberDominantColorState(
    imageUrl: String?
): State<DominantColorState> {
    val context = LocalContext.current
    val dominantColorState = remember { mutableStateOf<DominantColorState>(DominantColorState.Loading) }

    LaunchedEffect(imageUrl) {
        dominantColorState.value = DominantColorState.Loading
        dominantColorState.value = DominantColorExtractor.extractDominantColor(
            imageUrl = imageUrl,
            context = context
        )
    }

    return dominantColorState
}

@Composable
fun rememberDominantColor(
    imageUrl: String?,
    fallbackColor: Color = Color.Gray
): State<Color> {
    val colorState by rememberDominantColorState(imageUrl)
    val dominantColor = remember { mutableStateOf(fallbackColor) }

    LaunchedEffect(colorState) {
        when (val state = colorState) {
            is DominantColorState.Success -> dominantColor.value = state.color
            else -> dominantColor.value = fallbackColor
        }
    }

    return dominantColor
}