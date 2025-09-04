package com.example.pokedexapp.ui.screen.detail.components

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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonStats
import com.example.pokedexapp.ui.theme.attack
import com.example.pokedexapp.ui.theme.defense
import com.example.pokedexapp.ui.theme.hp
import com.example.pokedexapp.ui.theme.specialAttack
import com.example.pokedexapp.ui.theme.specialDefense
import com.example.pokedexapp.ui.theme.speed
import com.example.pokedexapp.ui.theme.statSheetTitle
import com.example.pokedexapp.util.Constants
import kotlinx.coroutines.delay

@Composable
fun StatBottomSheet(
    stats: PokemonStats,
    isExpanded: Boolean,
    isSheetVisible: Boolean = false,
    onCollapsedSizeChanged: (IntSize) -> Unit = {}
) {
    var animationTrigger by remember { mutableStateOf(Constants.DEFAULT_ANIMATION_TRIGGER) }

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            delay(Constants.STAT_SHEET_ANIMATION_DELAY)
            animationTrigger++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                if (!isExpanded) {
                    onCollapsedSizeChanged(coordinates.size)
                }
            }
            .padding(dimensionResource(R.dimen.stat_sheet_padding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.base_stats_title),
            fontSize = dimensionResource(R.dimen.stat_sheet_title_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            color = statSheetTitle,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.stat_sheet_title_bottom_padding))
        )

        key(animationTrigger) {
            StatBar(
                statName = stringResource(R.string.stat_hp_short),
                statValue = stats.hp,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = hp,
                isVisible = isSheetVisible,
                animationDelay = Constants.STAT_HP_ANIMATION_DELAY
            )

            StatBar(
                statName = stringResource(R.string.stat_attack_short),
                statValue = stats.attack,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = attack,
                isVisible = isSheetVisible,
                animationDelay = Constants.STAT_ATTACK_ANIMATION_DELAY
            )
        }

        if (isExpanded) {
            StatBar(
                statName = stringResource(R.string.stat_defense_short),
                statValue = stats.defense,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = defense,
                isVisible = true,
                animationDelay = Constants.STAT_DEFENSE_ANIMATION_DELAY
            )

            StatBar(
                statName = stringResource(R.string.stat_speed_short),
                statValue = stats.speed,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = speed,
                isVisible = true,
                animationDelay = Constants.STAT_SPEED_ANIMATION_DELAY
            )

            StatBar(
                statName = stringResource(R.string.stat_special_defense_short),
                statValue = stats.specialDefense,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = specialDefense,
                isVisible = true,
                animationDelay = Constants.STAT_SPECIAL_DEFENSE_ANIMATION_DELAY
            )

            StatBar(
                statName = stringResource(R.string.stat_special_attack_short),
                statValue = stats.specialAttack,
                statMaxValue = Constants.MAX_STAT_VALUE,
                barColor = specialAttack,
                isVisible = true,
                animationDelay = Constants.STAT_SPECIAL_ATTACK_ANIMATION_DELAY
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.stat_sheet_bottom_spacer)))
    }
}