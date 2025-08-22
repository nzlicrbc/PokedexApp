package com.example.pokedexapp.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: PokemonDetailEvent) {
        when (event) {
            is PokemonDetailEvent.OnLoadPokemonDetail -> getPokemonDetail()
        }
    }

    private fun getPokemonDetail() {
        val flow = repository.getPokemonDetail(pokemonId)

        safeLaunch(
            flow = flow,
            onLoading = {
                _uiState.update { it.copy(isLoading = true, error = null) }
            },
            onSuccess = { detail ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pokemonDetail = detail
                    )
                }
            },
            onError = { message ->
                _uiState.update {
                    it.copy(isLoading = false, error = message, pokemonDetail = null)
                }
            }
        )
    }
}