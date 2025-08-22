package com.example.pokedexapp.ui.screen.list

sealed interface PokemonListEvent {
    data object OnLoadPokemons : PokemonListEvent
    data object OnLoadMorePokemons : PokemonListEvent
    data object OnClearError : PokemonListEvent
}