package com.example.pokedexapp.ui.screen

import android.R
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.ui.screen.components.PokemonItem
import com.example.pokedexapp.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val pokemonList = viewModel.pokemonList.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value

    PokemonListContent(
        pokemonList = pokemonList,
        isLoading = isLoading,
        error = error,
        onPokemonClick = { pokemonId ->
            navController.navigate("pokemon_detail/$pokemonId")
        },
        onLoadMore = { viewModel.loadMorePokemons() },
        onRetry = { viewModel.loadPokemons() },
        onClearError = { viewModel.clearError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    pokemonList: List<Pokemon>,
    isLoading: Boolean,
    error: String?,
    onPokemonClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    Scaffold(
        topBar = {PokemonListTopBar()}
    ) {
        paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF2B2929))
        ) {
            when {
                isLoading && pokemonList.isEmpty() -> {
                    LoadingView()
                }
                error != null && pokemonList.isEmpty() -> {
                    ErrorView(
                        error = error,
                        onRetry = onRetry
                    )
                }
                else -> {
                    PokemonGridContent(
                        pokemonList = pokemonList,
                        onPokemonClick = onPokemonClick,
                        onLoadMore = onLoadMore,
                        isLoading = isLoading
                    )
                }
            }

            if(error != null && pokemonList.isNotEmpty()) {
                PaginationErrorSnackbar(
                    error = error,
                    onRetry = {
                        onClearError()
                        onLoadMore()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListTopBar() {
    TopAppBar(
        title= {
            Text(
                text = "Pokedex",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFD53B3B)
        ),
        modifier = Modifier.statusBarsPadding()
    )
}

@Composable
private fun LoadingView(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFE3350D)
        )
    }
}

@Composable
private fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE3350D)
            )
        ) {
            Text("Tekrar Dene")
        }
    }
}

@Composable
private fun PokemonGridContent(
    pokemonList: List<Pokemon>,
    onPokemonClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    isLoading: Boolean
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= totalItemsCount - 4 && !isLoading
        }
    }

    LaunchedEffect( shouldLoadMore.value) {
        if (shouldLoadMore.value && pokemonList.isNotEmpty()) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(
            items = pokemonList,
            key = { _, pokemon -> pokemon.id }
        ) { _, pokemon ->
            PokemonItem(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) }
            )
        }

        if(isLoading && pokemonList.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                PaginationLoadingItem()
            }
        }
    }
}

@Composable
private fun PaginationLoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFE3350D),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun PaginationErrorSnackbar(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
){
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
            TextButton(onClick = onRetry) {
                Text("Tekrar Dene", color = Color.White)
            }
        }
    ) {
        Text(text = error)
    }
}