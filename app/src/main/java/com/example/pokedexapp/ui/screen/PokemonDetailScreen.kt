package com.example.pokedexapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.ui.screen.components.StatBottomSheet
import com.example.pokedexapp.ui.screen.components.TypeBadge
import com.example.pokedexapp.ui.theme.bottomSheetBackground
import com.example.pokedexapp.ui.theme.errorText
import com.example.pokedexapp.ui.theme.measurementLabel
import com.example.pokedexapp.ui.theme.measurementValue
import com.example.pokedexapp.ui.theme.pokemonDetailName
import com.example.pokedexapp.ui.theme.screenBackground
import com.example.pokedexapp.ui.theme.statsButton
import com.example.pokedexapp.ui.theme.statsButtonText
import com.example.pokedexapp.ui.theme.topBarText
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.util.rememberDominantColor
import com.example.pokedexapp.viewmodel.PokemonViewModel
import kotlinx.coroutines.launch
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

    var collapsedHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(pokemonId) {
        viewModel.getPokemonDetail(pokemonId)
    }

    val dominantColor by rememberDominantColor(
        imageUrl = selectedPokemon?.imageUrl
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = collapsedHeight,
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
                    IconButton(
                        onClick = {
                            viewModel.clearSelectedPokemon()
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            tint = topBarText
                        )
                    }
                },
                actions = {
                    Text(
                        text = if(selectedPokemon?.id != null) {
                            "${stringResource(R.string.pokemon_id_prefix)}${selectedPokemon?.id.toString().padStart(Constants.POKEMON_ID_PADDING, '0')}"
                        } else {
                            ""
                        },
                        fontSize = dimensionResource(R.dimen.pokemon_id_text_size).value.sp,
                        color = topBarText,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dominantColor
                )
            )
        },
        sheetContent = {
            if (selectedPokemon != null) {
                StatBottomSheet(
                    stats = selectedPokemon.stats,
                    isExpanded = bottomSheetState.currentValue == SheetValue.Expanded,
                    isSheetVisible = bottomSheetState.currentValue != SheetValue.Hidden,
                    onCollapsedSizeChanged = { size ->
                        collapsedHeight = with(density) { size.height.toDp() }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.error_screen_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = error, color = errorText)
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.error_screen_spacer_height)))
                        Button(onClick = {
                            viewModel.clearError()
                            viewModel.getPokemonDetail(pokemonId)
                        }) {
                            Text(stringResource(R.string.retry_button_text))
                        }
                    }
                }

                selectedPokemon != null -> {
                    PokemonDetailContent(
                        pokemonDetail = selectedPokemon,
                        topColor = dominantColor,
                        onStatsClick = {
                            scope.launch {
                                when (bottomSheetState.currentValue) {
                                    SheetValue.Hidden -> {
                                        bottomSheetState.partialExpand()
                                    }

                                    SheetValue.PartiallyExpanded -> {
                                        bottomSheetState.expand()
                                    }

                                    SheetValue.Expanded -> {
                                        bottomSheetState.hide()
                                    }
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
fun PokemonDetailContent(
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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