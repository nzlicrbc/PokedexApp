package com.example.pokedexapp.domain.repository

import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): Flow<Resource<List<Pokemon>>>

    fun getPokemonDetail(
        pokemonId: Int
    ): Flow<Resource<PokemonDetail>>
}