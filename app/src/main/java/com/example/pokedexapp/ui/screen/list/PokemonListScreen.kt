package com.example.pokedexapp.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.pokedexapp.R
import com.example.pokedexapp.navigation.NavigationRoute
import com.example.pokedexapp.ui.screen.list.components.PokemonItem
import com.example.pokedexapp.ui.theme.*
import com.example.pokedexapp.util.Constants

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(PokemonListEvent.OnLoadPokemons)
    }

    PokemonListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onPokemonClick = { pokemonId ->
            navController.navigate(NavigationRoute.PokemonDetail.createRoute(pokemonId))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    uiState: PokemonListUiState,
    onEvent: (PokemonListEvent) -> Unit,
    onPokemonClick: (Int) -> Unit
) {
    Scaffold(
        topBar = { PokemonListTopBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(pokemonListBackground)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingView()
                }
                uiState.error != null && uiState.pokemonList.isEmpty() -> {
                    ErrorView(
                        error = uiState.error,
                        onRetry = { onEvent(PokemonListEvent.OnLoadPokemons) }
                    )
                }
                else -> {
                    PokemonGridContent(
                        uiState = uiState,
                        onPokemonClick = onPokemonClick,
                        onEvent = onEvent
                    )
                }
            }

            if (uiState.error != null && uiState.pokemonList.isNotEmpty()) {
                PaginationErrorSnackbar(
                    error = uiState.error,
                    onRetry = {
                        onEvent(PokemonListEvent.OnClearError)
                        onEvent(PokemonListEvent.OnLoadMorePokemons)
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun PokemonGridContent(
    uiState: PokemonListUiState,
    onPokemonClick: (Int) -> Unit,
    onEvent: (PokemonListEvent) -> Unit
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            if (totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

            lastVisibleItemIndex >= totalItemsCount - Constants.LOAD_MORE_THRESHOLD
                    && !uiState.isPaginating
                    && !uiState.isLastPage
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onEvent(PokemonListEvent.OnLoadMorePokemons)
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
            items = uiState.pokemonList,
            key = { _, pokemon -> pokemon.id }
        ) { _, pokemon ->
            PokemonItem(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) }
            )
        }

        if (uiState.isPaginating) {
            item(span = { GridItemSpan(Constants.POKEMON_GRID_COLUMNS) }) {
                PaginationLoadingItem()
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = loadingIndicator)
    }
}

@Composable
private fun ErrorView(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.error_view_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = errorText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.error_text_bottom_padding))
        )
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = retryButton)) {
            Text(stringResource(R.string.retry_button_text))
        }
    }
}

@Composable
private fun PaginationLoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.pagination_loading_padding)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = paginationLoadingIndicator,
            modifier = Modifier.size(dimensionResource(R.dimen.pagination_loading_size))
        )
    }
}

@Composable
private fun PaginationErrorSnackbar(error: String, onRetry: () -> Unit, modifier: Modifier = Modifier){
    Snackbar(
        modifier = modifier.padding(dimensionResource(R.dimen.snackbar_padding)),
        action = {
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.retry_button_text), color = snackbarActionText)
            }
        }
    ) {
        Text(text = error)
    }
}