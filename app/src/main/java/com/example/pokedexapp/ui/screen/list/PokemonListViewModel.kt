package com.example.pokedexapp.ui.screen.list

import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.util.Constants
import com.example.pokedexapp.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = Constants.INITIAL_PAGE
    private val pageSize = Constants.DEFAULT_POKEMON_LIST_LIMIT

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.OnLoadPokemons -> loadPokemons(isInitialLoad = true)
            is PokemonListEvent.OnLoadMorePokemons -> loadMorePokemons()
            is PokemonListEvent.OnClearError -> clearError()
        }
    }

    private fun loadPokemons(isInitialLoad: Boolean) {
        if (isInitialLoad) {
            _uiState.update { it.copy(pokemonList = emptyList()) }
            currentPage = Constants.INITIAL_PAGE
        }

        val flow = repository.getPokemonList(limit = pageSize, offset = 0)

        safeLaunch(
            flow = flow,
            onLoading = { _uiState.update { it.copy(isLoading = true, error = null) } },
            onSuccess = { pokemons ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pokemonList = pokemons ?: emptyList(),
                        isLastPage = (pokemons?.size ?: 0) < pageSize
                    )
                }
            },
            onError = { message ->
                _uiState.update { it.copy(isLoading = false, error = message) }
            }
        )
    }

    private fun loadMorePokemons() {
        val currentState = _uiState.value
        if (currentState.isPaginating || currentState.isLastPage || currentState.isLoading) return

        val nextPage = currentPage + Constants.PAGE_INCREMENT
        val flow = repository.getPokemonList(limit = pageSize, offset = nextPage * pageSize)

        safeLaunch(
            flow = flow,
            onLoading = { _uiState.update { it.copy(isPaginating = true, error = null) } },
            onSuccess = { newPokemons ->
                _uiState.update {
                    it.copy(
                        isPaginating = false,
                        pokemonList = it.pokemonList + (newPokemons ?: emptyList()),
                        isLastPage = (newPokemons?.size ?: 0) < pageSize
                    )
                }
                currentPage = nextPage
            },
            onError = { message ->
                _uiState.update { it.copy(isPaginating = false, error = message) }
            }
        )
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}