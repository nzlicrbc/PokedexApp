package com.example.pokedexapp.ui.screen.list

import com.example.pokedexapp.domain.model.Pokemon

data class PokemonListUiState(
    val isLoading: Boolean = true,
    val isPaginating: Boolean = false,
    val pokemonList: List<Pokemon> = emptyList(),
    val error: String? = null,
    val isLastPage: Boolean = false
)