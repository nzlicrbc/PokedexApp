package com.example.pokedexapp.ui.screen.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatBar(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    barColor: Color,
    isVisible: Boolean = true,
    animationDelay: Int = 0,
    modifier: Modifier = Modifier
){
    var animationPlayed by remember { mutableStateOf(false) }

    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            (statValue.toFloat() / statMaxValue.toFloat()).coerceIn(0f, 1f)
        } else 0f,
        animationSpec = tween(
            durationMillis = 1000,
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

    val textFitsInProgress = currentPercent.value > 0.18f

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.width(35.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentPercent.value)
                    .clip(RoundedCornerShape(12.dp))
                    .background(barColor)
            ) {
                if (textFitsInProgress) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "$statValue/$statMaxValue",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$statValue/$statMaxValue",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

object StatColors {
    val HP = Color(0xFFD43A47)
    val ATTACK = Color(0xFFFEA726)
    val DEFENSE = Color(0xFF0590E8)
    val SPEED = Color(0xFF8FB0C4)
    val SPECIAL_ATTACK = Color(0xFF378E3B)
    val SPECIAL_DEFENSE = Color(0xFF9C27B0)
}