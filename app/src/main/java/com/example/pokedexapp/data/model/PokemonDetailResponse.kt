package com.example.pokedexapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("types")
    val types: List<TypeSlot>,
    @SerializedName("stats")
    val stats: List<StatSlot>,
    @SerializedName("sprites")
    val sprites: Sprites
)

data class TypeSlot(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: Type
)

data class Type(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class StatSlot(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("stat")
    val stat: Stat
)

data class Stat(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?
)