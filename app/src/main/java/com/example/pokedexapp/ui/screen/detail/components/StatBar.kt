package com.example.pokedexapp.ui.screen.detail.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.ui.theme.statBarBackground
import com.example.pokedexapp.ui.theme.statNameText
import com.example.pokedexapp.ui.theme.statValueTextDark
import com.example.pokedexapp.ui.theme.statValueTextLight
import com.example.pokedexapp.util.Constants

@Composable
fun StatBar(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    barColor: Color,
    isVisible: Boolean = true,
    animationDelay: Int = Constants.DEFAULT_ANIMATION_DELAY,
    modifier: Modifier = Modifier
){
    var animationPlayed by remember { mutableStateOf(false) }

    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            (statValue.toFloat() / statMaxValue.toFloat()).coerceIn(Constants.MIN_STAT_PERCENT, Constants.MAX_STAT_PERCENT)
        } else Constants.MIN_STAT_PERCENT,
        animationSpec = tween(
            durationMillis = Constants.STAT_ANIMATION_DURATION,
            delayMillis = animationDelay
        ),
        label = "stat animation"
    )

    LaunchedEffect(key1 = isVisible) {
        if (isVisible) {
            animationPlayed = true
        } else {
            animationPlayed = false
        }
    }

    val textFitsInProgress = currentPercent.value > Constants.TEXT_VISIBILITY_THRESHOLD

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.stat_bar_vertical_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            fontSize = dimensionResource(R.dimen.stat_name_text_size).value.sp,
            fontWeight = FontWeight.Medium,
            color = statNameText,
            modifier = Modifier.width(dimensionResource(R.dimen.stat_name_width))
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.stat_bar_spacer_width)))

        Box(
            modifier = Modifier
                .weight(Constants.STAT_BAR_WEIGHT)
                .height(dimensionResource(R.dimen.stat_bar_height))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.stat_bar_corner_radius)))
                    .background(statBarBackground)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentPercent.value)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.stat_bar_corner_radius)))
                    .background(barColor)
            ) {
                if (textFitsInProgress) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = dimensionResource(R.dimen.stat_text_end_padding)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "$statValue/$statMaxValue",
                            fontSize = dimensionResource(R.dimen.stat_value_text_size).value.sp,
                            fontWeight = FontWeight.Bold,
                            color = statValueTextLight
                        )
                    }
                }
            }

            if (!textFitsInProgress) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.fillMaxWidth(currentPercent.value))
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.stat_text_spacer_width)))
                    Text(
                        text = "$statValue/$statMaxValue",
                        fontSize = dimensionResource(R.dimen.stat_value_text_size).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = statValueTextDark
                    )
                }
            }
        }
    }
}