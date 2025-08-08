package com.example.pokedexapp.viewmodel

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.model.PokemonDetail
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = mutableStateOf<List<Pokemon>>(emptyList())
    val pokemonList: State<List<Pokemon>> = _pokemonList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _selectedPokemon = mutableStateOf<PokemonDetail?>(null)
    val selectedPokemon: State<PokemonDetail?> = _selectedPokemon

    private val _isDetailLoading = mutableStateOf(false)
    val isDetailLoading: State<Boolean> = _isDetailLoading

    private var currentPage = 0
    private var isLastPage = false
    private var isLoadingMore = false
    private val pageSize = 20

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        repository.getPokemonList(
            limit = 20,
            offset = 0
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pokemonList.value = result.data ?: emptyList()
                    _isLoading.value = false
                    _error.value = null
                    currentPage = 0
                    isLastPage = (result.data?.size ?: 0) < PAGE_SIZE
                }

                is Resource.Error -> {
                    _isLoading.value = false
                    _error.value = result.message
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    _error.value = null
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadMorePokemons() {
        if (isLoadingMore || isLastPage || _isLoading.value) return

        isLoadingMore = true
        val nextPage = currentPage +1

        repository.getPokemonList(
            limit = 20,
            offset = nextPage * pageSize
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newPokemons = result.data ?: emptyList()
                    _pokemonList.value = _pokemonList.value + newPokemons
                    currentPage = nextPage
                    isLastPage = newPokemons.size < pageSize
                    isLoadingMore = false
                }
                is Resource.Error -> {
                    isLoadingMore = false
                    _error.value = result.message
                }
                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPokemonDetail(pokemonId: Int) {
        repository.getPokemonDetail(pokemonId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _selectedPokemon.value = result.data
                    _isDetailLoading.value = false
                    _error.value = null
                }
                is Resource.Error -> {
                    _isDetailLoading.value = false
                    _error.value = result.message
                    _selectedPokemon.value = null
                }
                is Resource.Loading -> {
                    _isDetailLoading.value = true
                    _error.value = null
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSelectedPokemon() {
        _selectedPokemon.value = null
    }
}