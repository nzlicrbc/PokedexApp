package com.example.pokedexapp.ui.screen.detail

sealed interface PokemonDetailEvent {
    data object OnLoadPokemonDetail : PokemonDetailEvent
}