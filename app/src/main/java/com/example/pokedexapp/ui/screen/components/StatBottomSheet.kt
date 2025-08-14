package com.example.pokedexapp.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.domain.model.PokemonStats
import kotlinx.coroutines.delay

@Composable
fun StatBottomSheet(
    stats: PokemonStats,
    isExpanded: Boolean,
    isSheetVisible: Boolean = false
) {
    var animationTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            delay(50)
            animationTrigger++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Base Stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        key(animationTrigger) {
            StatBar(
                statName = "HP",
                statValue = stats.hp,
                statMaxValue = 300,
                barColor = StatColors.HP,
                isVisible = isSheetVisible,
                animationDelay = 100
            )

            StatBar(
                statName = "ATK",
                statValue = stats.attack,
                statMaxValue = 300,
                barColor = StatColors.ATTACK,
                isVisible = isSheetVisible,
                animationDelay = 200
            )
        }

        if (isExpanded) {
            StatBar(
                statName = "DEF",
                statValue = stats.defense,
                statMaxValue = 300,
                barColor = StatColors.DEFENSE,
                isVisible = true,
                animationDelay = 300
            )

            StatBar(
                statName = "SPD",
                statValue = stats.speed,
                statMaxValue = 300,
                barColor = StatColors.SPEED,
                isVisible = true,
                animationDelay = 400
            )

            StatBar(
                statName = "S.DEF",
                statValue = stats.specialDefense,
                statMaxValue = 300,
                barColor = StatColors.SPECIAL_DEFENSE,
                isVisible = true,
                animationDelay = 500
            )

            StatBar(
                statName = "EXP",
                statValue = stats.specialAttack,
                statMaxValue = 1000,
                barColor = StatColors.SPECIAL_ATTACK,
                isVisible = true,
                animationDelay = 600
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}