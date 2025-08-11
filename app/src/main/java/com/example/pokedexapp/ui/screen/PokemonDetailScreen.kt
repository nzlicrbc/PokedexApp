package com.example.pokedexapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.viewmodel.PokemonViewModel
import java.nio.file.WatchEvent
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val selectedPokemon = viewModel.selectedPokemon.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value

    LaunchedEffect(pokemonId) {
        viewModel.getPokemonDetail(pokemonId)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedPokemon?.name?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                            else it.toString()
                        } ?: "Pokemon",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearSelectedPokemon()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.clearError()
                                viewModel.getPokemonDetail(pokemonId)
                            }
                        ) {
                            Text("Tekrar dene")
                        }
                    }
                }

                selectedPokemon != null -> {
                    PokemonDetailContent(pokemonDetail = selectedPokemon)
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(pokemonDetail: PokemonDetail) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = pokemonDetail.imageUrl,
            contentDescription = pokemonDetail.name,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = pokemonDetail.name.replaceFirstChar {
                if(it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "#${pokemonDetail.id.toString().padStart(3, '0')}",
            fontSize=18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "types: ${pokemonDetail.types.joinToString (", ")}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "weight: ${pokemonDetail.weight}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "height: ${pokemonDetail.height}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "base stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val stats = pokemonDetail.stats
        Text(
            text = "hp: ${stats.hp}",
            fontSize = 16.sp
        )
        Text(
            text = "attack: ${stats.attack}",
            fontSize = 16.sp
        )
        Text(
            text = "defense: ${stats.defense}",
            fontSize = 16.sp
        )
        Text(
            text = "speed: ${stats.speed}",
            fontSize = 16.sp
        )
        Text(
            text = "special attack: ${stats.specialAttack}",
            fontSize = 16.sp
        )
        Text(
            text = "special defense: ${stats.specialDefense}",
            fontSize = 16.sp
        )
    }
}