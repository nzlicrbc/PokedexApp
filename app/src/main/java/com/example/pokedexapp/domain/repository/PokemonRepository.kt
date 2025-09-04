package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(
        limit: Int = Constants.DEFAULT_POKEMON_LIST_LIMIT,
        offset: Int = Constants.DEFAULT_POKEMON_LIST_OFFSET
    ): Flow<Resource<List<Pokemon>>>

    fun getPokemonDetail(
        pokemonId: Int
    ): Flow<Resource<PokemonDetail>>
}