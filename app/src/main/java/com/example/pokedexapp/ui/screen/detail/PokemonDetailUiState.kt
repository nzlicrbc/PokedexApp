package com.example.pokedexapp.ui.screen.detail

import com.example.pokedexapp.domain.model.PokemonDetail

data class PokemonDetailUiState(
    val isLoading: Boolean = true,
    val pokemonDetail: PokemonDetail? = null,
    val error: String? = null
)