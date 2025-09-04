package com.example.pokedexapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel : ViewModel() {
    protected fun <T> safeLaunch(
        flow: Flow<Resource<T>>,
        onLoading: () -> Unit,
        onSuccess: (T?) -> Unit,
        onError: (String?) -> Unit
    ) {
        flow.onEach { result ->
            when (result) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(result.data)
                is Resource.Error -> onError(result.message)
            }
        }.launchIn(viewModelScope)
    }
}