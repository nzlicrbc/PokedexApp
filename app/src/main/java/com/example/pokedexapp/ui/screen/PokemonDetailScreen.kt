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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.ui.screen.components.StatBottomSheet
import com.example.pokedexapp.ui.screen.components.TypeBadge
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
        sheetPeekHeight = if (bottomSheetState.currentValue == SheetValue.Hidden) 0.dp else 200.dp,
        sheetContainerColor = Color(43, 41, 44),
        containerColor = Color(43, 41, 44),
        sheetSwipeEnabled = bottomSheetState.currentValue != SheetValue.Hidden,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pokedex",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Text(
                        text = if(selectedPokemon?.id != null) {
                            "#${selectedPokemon?.id.toString().padStart(3, '0')}"
                        } else {
                            ""
                        },
                        fontSize = 18.sp,
                        color = Color.White,
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
                    isSheetVisible = bottomSheetState.currentValue != SheetValue.Hidden
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = error, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.clearError()
                            viewModel.getPokemonDetail(pokemonId)
                        }) {
                            Text("Tekrar dene")
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
            .background(Color(43, 41, 44))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            topColor,
                            topColor.copy(alpha = 0.9f),
                            topColor.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pokemonDetail.imageUrl,
                contentDescription = pokemonDetail.name,
                modifier = Modifier.size(250.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = pokemonDetail.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                    else it.toString()
                },
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(pokemonDetail.types) { type ->
                    TypeBadge(typeName = type)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${pokemonDetail.weight}",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "weight",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${pokemonDetail.height}",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "height",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStatsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(213,59,71))
            ) {
                Text(
                    text = "Base Stats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}