package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.repository.PokemonRepository
import kotlinx.coroutines.launch
class DatosViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemons = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemons: LiveData<List<Pokemon>> = _pokemons

    private val _pokemonDetalle = MutableLiveData<Pokemon?>(null)
    val pokemonDetalle: LiveData<Pokemon?> = _pokemonDetalle

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun cargarPokemons() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getPokemonsList()
            result.onSuccess {
                _pokemons.value = it
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun cargarPokemonPorId(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getPokemonById(id)
            result.onSuccess {
                _pokemonDetalle.value = it
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

}