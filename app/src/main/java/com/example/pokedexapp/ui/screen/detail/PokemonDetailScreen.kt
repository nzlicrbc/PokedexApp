package com.example.pokedexapp.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.ui.screen.detail.components.StatBottomSheet
import com.example.pokedexapp.ui.screen.detail.components.TypeBadge
import com.example.pokedexapp.ui.theme.*
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.util.rememberDominantColor
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PokemonDetailScreen(
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(PokemonDetailEvent.OnLoadPokemonDetail)
    }

    PokemonDetailContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailContent(
    uiState: PokemonDetailUiState,
    onEvent: (PokemonDetailEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var collapsedHeight by remember { mutableStateOf(0.dp) }
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val finalPeekHeight = if (collapsedHeight > 0.dp) {
        collapsedHeight + navBarHeight
    } else {
        0.dp
    }

    val density = LocalDensity.current
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val scope = rememberCoroutineScope()

    val dominantColor by rememberDominantColor(
        imageUrl = uiState.pokemonDetail?.imageUrl
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = finalPeekHeight,
        sheetContainerColor = bottomSheetBackground,
        containerColor = screenBackground,
        sheetSwipeEnabled = bottomSheetState.currentValue != SheetValue.Hidden,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = dimensionResource(R.dimen.top_bar_title_text_size).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = topBarText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            tint = topBarText
                        )
                    }
                },
                actions = {
                    uiState.pokemonDetail?.let {
                        Text(
                            text = "#${it.id.toString().padStart(Constants.POKEMON_ID_PADDING, '0')}",
                            fontSize = dimensionResource(R.dimen.pokemon_id_text_size).value.sp,
                            color = topBarText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dominantColor
                )
            )
        },
        sheetContent = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                uiState.pokemonDetail?.let {
                    StatBottomSheet(
                        stats = it.stats,
                        isExpanded = bottomSheetState.currentValue == SheetValue.Expanded,
                        isSheetVisible = bottomSheetState.currentValue != SheetValue.Hidden,
                        onCollapsedSizeChanged = { size ->
                            collapsedHeight = with(density) { size.height.toDp() }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null && uiState.pokemonDetail == null -> {
                    ErrorView(
                        error = uiState.error,
                        onRetry = { onEvent(PokemonDetailEvent.OnLoadPokemonDetail) }
                    )
                }
                uiState.pokemonDetail != null -> {
                    PokemonDetailMainContent(
                        pokemonDetail = uiState.pokemonDetail,
                        topColor = dominantColor,
                        onStatsClick = {
                            scope.launch {
                                when (bottomSheetState.currentValue) {
                                    SheetValue.Hidden -> bottomSheetState.partialExpand()
                                    SheetValue.PartiallyExpanded -> bottomSheetState.expand()
                                    SheetValue.Expanded -> bottomSheetState.hide()
                                }
                            }
                        }
                    )
                }
            }
        }
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
            .padding(dimensionResource(R.dimen.error_screen_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = error, color = errorText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.error_screen_spacer_height)))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry_button_text))
        }
    }
}

@Composable
private fun PokemonDetailMainContent(
    pokemonDetail: PokemonDetail,
    topColor: Color,
    onStatsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.pokemon_image_container_height))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            topColor,
                            topColor.copy(alpha = Constants.GRADIENT_ALPHA_HIGH),
                            topColor.copy(alpha = Constants.GRADIENT_ALPHA_LOW)
                        )
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = dimensionResource(R.dimen.pokemon_image_container_corner_radius),
                        bottomEnd = dimensionResource(R.dimen.pokemon_image_container_corner_radius)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pokemonDetail.imageUrl,
                contentDescription = pokemonDetail.name,
                modifier = Modifier.size(dimensionResource(R.dimen.pokemon_detail_image_size))
            )
        }

        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.pokemon_detail_content_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pokemon_name_top_spacer)))

            Text(
                text = pokemonDetail.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else it.toString()
                },
                fontSize = dimensionResource(R.dimen.pokemon_name_text_size_detail).value.sp,
                color = pokemonDetailName,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pokemon_types_top_spacer)))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.type_badges_spacing)),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.type_badges_vertical_padding))
            ) {
                items(pokemonDetail.types) { type ->
                    TypeBadge(typeName = type)
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.pokemon_measurements_top_spacer)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${pokemonDetail.weight}",
                        fontSize = dimensionResource(R.dimen.measurement_value_text_size).value.sp,
                        color = measurementValue
                    )
                    Text(
                        text = stringResource(R.string.weight_label),
                        fontSize = dimensionResource(R.dimen.measurement_label_text_size).value.sp,
                        color = measurementLabel
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${pokemonDetail.height}",
                        fontSize = dimensionResource(R.dimen.measurement_value_text_size).value.sp,
                        color = measurementValue
                    )
                    Text(
                        text = stringResource(R.string.height_label),
                        fontSize = dimensionResource(R.dimen.measurement_label_text_size).value.sp,
                        color = measurementLabel
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.stats_button_top_spacer)))

            Button(
                onClick = onStatsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = statsButton)
            ) {
                Text(
                    text = stringResource(R.string.base_stats_button_text),
                    fontSize = dimensionResource(R.dimen.stats_button_text_size).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = statsButtonText
                )
            }
        }
    }
}