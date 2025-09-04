package com.example.pokedexapp.ui.screen.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.ui.theme.pokemonNameText
import com.example.pokedexapp.ui.theme.screenBackground
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.util.DominantColorState
import com.example.pokedexapp.util.rememberDominantColorState
import java.util.Locale

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val dominantColorState by rememberDominantColorState(imageUrl = pokemon.imageUrl)

    val backgroundColor by animateColorAsState(
        targetValue = when (val state = dominantColorState) {
            is DominantColorState.Success -> state.color.copy(alpha = Constants.POKEMON_ITEM_BACKGROUND_ALPHA)
            else -> screenBackground
        },
        animationSpec = tween(durationMillis = Constants.COLOR_ANIMATION_DURATION_MS),
        label = "backgroundColor"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(Constants.POKEMON_ITEM_ASPECT_RATIO),
        shape = RoundedCornerShape(dimensionResource(R.dimen.pokemon_item_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.pokemon_item_elevation)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            when (dominantColorState) {
                is DominantColorState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.pokemon_item_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(pokemon.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = pokemon.name,
                            modifier = Modifier
                                .fillMaxWidth(Constants.POKEMON_IMAGE_WIDTH_RATIO)
                                .aspectRatio(Constants.POKEMON_IMAGE_ASPECT_RATIO),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pokemon_item_loading_spacer_height)))

                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensionResource(R.dimen.pokemon_item_loading_indicator_size)),
                            strokeWidth = dimensionResource(R.dimen.pokemon_item_loading_indicator_stroke),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is DominantColorState.Success, is DominantColorState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.pokemon_item_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(pokemon.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = pokemon.name,
                            modifier = Modifier
                                .fillMaxWidth(Constants.POKEMON_IMAGE_WIDTH_RATIO)
                                .aspectRatio(Constants.POKEMON_IMAGE_ASPECT_RATIO),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pokemon_item_spacer_height)))

                        Text(
                            text = pokemon.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                else it.toString()
                            },
                            fontSize = dimensionResource(R.dimen.pokemon_name_text_size).value.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = pokemonNameText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}