package com.example.pokedexapp.data.remote

import com.example.pokedexapp.BuildConfig
import com.example.pokedexapp.data.model.PokemonDetailResponse
import com.example.pokedexapp.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Header("Authorization") auth: String = "Bearer ${BuildConfig.UUID}",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetailById(
        @Header("Authorization") auth: String = "Bearer ${BuildConfig.UUID}",
        @Path("id") id: Int
    ): PokemonDetailResponse
}