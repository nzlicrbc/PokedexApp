package com.example.pokedexapp.data.repository

import android.content.Context
import com.example.pokedexapp.R
import com.example.pokedexapp.data.remote.ApiService
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.domain.model.PokemonStats
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : PokemonRepository {

    override fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Flow<Resource<List<Pokemon>>> =flow {
        emit(Resource.Loading())

        try {
            val response = apiService.getPokemonList(limit = limit, offset = offset)

            val pokemonList = coroutineScope {
                response.results.map { pokemonResult ->
                    async {
                        try {
                            val id = pokemonResult.getPokemonId()
                            val detailResponse = apiService.getPokemonDetailById(id = id)
                            Pokemon(
                                id = id,
                                name = pokemonResult.name,
                                imageUrl = "${Constants.POKEMON_SPRITE_BASE_URL}$id.png",
                                types = detailResponse.types.sortedBy { it.slot }
                                    .map { it.type.name }
                            )
                        } catch (e: Exception) {
                            val id = pokemonResult.getPokemonId()
                            Pokemon(
                                id = id,
                                name = pokemonResult.name,
                                imageUrl = "${Constants.POKEMON_SPRITE_BASE_URL}$id.png",
                                types = emptyList()
                            )
                        }
                    }
                }.awaitAll()
            }
            emit(Resource.Success(pokemonList))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "${context.getString(R.string.error_occurred)} ${e.localizedMessage}"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = context.getString(R.string.check_internet_connection)
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "${context.getString(R.string.unexpected_error_occurred)} ${e.localizedMessage}"
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
                weight = "${response.weight / Constants.WEIGHT_CONVERSION_FACTOR} ${context.getString(R.string.unit_kg)}",
                height = "${response.height / Constants.HEIGHT_CONVERSION_FACTOR} ${context.getString(R.string.unit_m)}",
                stats = mapStats(response.stats)
            )

            emit(Resource.Success(pokemonDetail))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "${context.getString(R.string.pokemon_details_load_error)} ${e.localizedMessage}"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = context.getString(R.string.check_internet_connection)
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = "${context.getString(R.string.unexpected_error_occurred)} ${e.localizedMessage}"
            ))
        }
    }

    private fun mapStats(stats: List<com.example.pokedexapp.data.model.StatSlot>): PokemonStats {
        val statsMap = stats.associate { it.stat.name to it.baseStat }

        return PokemonStats(
            hp = statsMap[Constants.STAT_HP] ?: Constants.DEFAULT_STAT_VALUE,
            attack = statsMap[Constants.STAT_ATTACK] ?: Constants.DEFAULT_STAT_VALUE,
            defense = statsMap[Constants.STAT_DEFENSE] ?: Constants.DEFAULT_STAT_VALUE,
            speed = statsMap[Constants.STAT_SPEED] ?: Constants.DEFAULT_STAT_VALUE,
            specialAttack = statsMap[Constants.STAT_SPECIAL_ATTACK] ?: Constants.DEFAULT_STAT_VALUE,
            specialDefense = statsMap[Constants.STAT_SPECIAL_DEFENSE] ?: Constants.DEFAULT_STAT_VALUE
        )
    }
}