package com.example.pokedexapp.data.repository

import retrofit2.HttpException
import com.example.pokedexapp.data.remote.ApiService
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.domain.model.PokemonStats
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository {

    override fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Flow<Resource<List<Pokemon>>> =flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getPokemonList(limit = limit, offset = offset)
            val pokemonList = response.results.map {pokemonResult ->
                val id = pokemonResult.getPokemonId()
                Pokemon(
                    id = id,
                    name = pokemonResult.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                )
            }
            emit(Resource.Success(pokemonList))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Bir hata oluştu: ${e.localizedMessage}"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "İnternet bağlantınızı kontrol edin"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Beklenmeyen bir hata oluştu: ${e.localizedMessage}"
            ))
        }
    }

    override fun getPokemonDetail(
        pokemonId: Int
    ): Flow<Resource<PokemonDetail>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getPokemonDetailById(id = pokemonId)

            val pokemonDetail = PokemonDetail(
                id = response.id,
                name = response.name,
                imageUrl = response.sprites.frontDefault ?: "",
                types = response.types.sortedBy { it.slot }.map { it.type.name },
                weight = "${response.weight / 10.0} KG",
                height = "${response.height / 10.0} M",
                stats = mapStats(response.stats)
            )

            emit(Resource.Success(pokemonDetail))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Pokemon detayları yüklenemedi: ${e.localizedMessage}"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "İnternet bağlantınızı kontrol edin"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Beklenmeyen bir hata oluştu: ${e.localizedMessage}"
            ))
        }
    }

    private fun mapStats(stats: List<com.example.pokedexapp.data.model.StatSlot>): PokemonStats {
        val statsMap = stats.associate { it.stat.name to it.baseStat }

        return PokemonStats(
            hp = statsMap["hp"] ?: 0,
            attack = statsMap["attack"] ?: 0,
            defense = statsMap["defense"] ?: 0,
            speed = statsMap["speed"] ?: 0,
            specialAttack = statsMap["special-attack"] ?: 0,
            specialDefense = statsMap["special-defense"] ?: 0
        )
    }
}