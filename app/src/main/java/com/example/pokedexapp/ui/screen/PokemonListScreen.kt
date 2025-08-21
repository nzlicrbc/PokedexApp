package com.example.pokedexapp.ui.screen

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.navigation.NavigationRoute
import com.example.pokedexapp.ui.screen.components.PokemonItem
import com.example.pokedexapp.ui.theme.errorText
import com.example.pokedexapp.ui.theme.loadingIndicator
import com.example.pokedexapp.ui.theme.paginationLoadingIndicator
import com.example.pokedexapp.ui.theme.pokemonListBackground
import com.example.pokedexapp.ui.theme.pokemonListTitle
import com.example.pokedexapp.ui.theme.pokemonListTopBar
import com.example.pokedexapp.ui.theme.retryButton
import com.example.pokedexapp.ui.theme.snackbarActionText
import com.example.pokedexapp.util.Constants
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
            navController.navigate(NavigationRoute.PokemonDetail.createRoute(pokemonId))
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(pokemonListBackground)
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
                text = stringResource(R.string.app_name),
                fontSize = dimensionResource(R.dimen.pokemon_list_title_text_size).value.sp,
                fontWeight = FontWeight.Bold,
                color = pokemonListTitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = pokemonListTopBar
        )
    )
}

@Composable
private fun LoadingView(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = loadingIndicator
        )
    }
}

@Composable
private fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.error_view_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = errorText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.error_text_bottom_padding))
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = retryButton
            )
        ) {
            Text(stringResource(R.string.retry_button_text))
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
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Constants.DEFAULT_LAST_VISIBLE_INDEX
            lastVisibleItemIndex >= totalItemsCount - Constants.LOAD_MORE_THRESHOLD && !isLoading
        }
    }

    LaunchedEffect( shouldLoadMore.value) {
        if (shouldLoadMore.value && pokemonList.isNotEmpty()) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(Constants.POKEMON_GRID_COLUMNS),
        state = gridState,
        contentPadding = PaddingValues(dimensionResource(R.dimen.pokemon_grid_content_padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.pokemon_grid_horizontal_spacing)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.pokemon_grid_vertical_spacing)),
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
            item(span = { GridItemSpan(Constants.POKEMON_GRID_COLUMNS) }) {
                PaginationLoadingItem()
            }
        }
    }
}

@Composable
private fun PaginationLoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.pagination_loading_padding)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = paginationLoadingIndicator,
            modifier = Modifier.size(dimensionResource(R.dimen.pagination_loading_size))
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
        modifier = Modifier.padding(dimensionResource(R.dimen.snackbar_padding)),
        action = {
            TextButton(onClick = onRetry) {
                Text(
                    text = stringResource(R.string.retry_button_text),
                    color = snackbarActionText
                )
            }
        }
    ) {
        Text(text = error)
    }
}