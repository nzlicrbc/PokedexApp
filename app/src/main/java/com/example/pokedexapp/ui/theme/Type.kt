package com.example.pokedexapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

fun getTypeColor(type: String): Color {
    return when(type.lowercase()) {
        "fire" -> Color(0xFFB22328)
        "water" -> Color(0xFF2648DC)
        "grass" -> Color(0xFF007C42)
        "electric" -> Color(0xFFE0E64B)
        "psychic" -> Color(0xFFAC296B)
        "ice" -> Color(0xFF7ECFF2)
        "dragon" -> Color(0xFF378A94)
        "dark" -> Color(0xFF040706)
        "fairy" -> Color(0xFF9E1A44)
        "normal" -> Color(0xFFB1A5A5)
        "fighting" -> Color(0xFF9F422A)
        "flying" -> Color(0xFF90B1C5)
        "poison" -> Color(0xFF642785)
        "ground" -> Color(0xFFAD7235)
        "rock" -> Color(0xFF4B190E)
        "bug" -> Color(0xFF179A55)
        "ghost" -> Color(0xFF363069)
        "steel" -> Color(0xFF5C756D)
        else -> Color(0xFFB1A5A5)
    }
}