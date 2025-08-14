package com.example.pokedexapp.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.domain.model.PokemonStats

@Composable
fun StatBottomSheet(
    stats: PokemonStats,
    isExpanded: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Base Stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        StatItem(label = "HP", value = stats.hp)
        StatItem(label = "Attack", value = stats.attack)

        if (isExpanded) {
            StatItem(label = "Defense", value = stats.defense)
            StatItem(label = "Speed", value = stats.speed)
            StatItem(label = "Special Attack", value = stats.specialAttack)
            StatItem(label = "Special Defense", value = stats.specialDefense)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value.toString(),
            fontSize = 16.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
    }
}