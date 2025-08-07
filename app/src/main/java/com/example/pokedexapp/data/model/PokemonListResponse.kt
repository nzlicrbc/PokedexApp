package com.example.pokedexapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonResult>
)

data class PokemonResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) {
    fun getPokemonId(): Int {
        return url.trimEnd('/').split('/').last().toIntOrNull() ?: 0
    }
}