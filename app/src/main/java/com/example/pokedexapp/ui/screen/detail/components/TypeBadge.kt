package com.example.pokedexapp.ui.screen.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.ui.theme.getTypeColor
import com.example.pokedexapp.ui.theme.typeBadgeText

@Composable
fun TypeBadge(typeName: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.type_badge_horizontal_padding))
            .width(dimensionResource(R.dimen.type_badge_width))
            .background(
                color = getTypeColor(typeName),
                shape = RoundedCornerShape(dimensionResource(R.dimen.type_badge_corner_radius))
            )
            .padding(vertical = dimensionResource(R.dimen.type_badge_vertical_padding)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = typeName,
            color = typeBadgeText,
            fontSize = dimensionResource(R.dimen.type_badge_text_size).value.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}