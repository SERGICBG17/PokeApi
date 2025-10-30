package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que expone estado para:
 * - Lista de Pokémon (ApiScreen)
 * - Detalle de Pokémon (DatosDelPokemonScreen)
 * - Estados de carga y error uniformes
 */
class VistaViewModel : ViewModel() {

    private val repositorio = PokemonRepository()

    // Lista observable por la UI
    private val _pokemons = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemons: StateFlow<List<Pokemon>> = _pokemons

    // Pokémon de detalle observable por la UI
    private val _pokemonDetalle = MutableStateFlow<Pokemon?>(null)
    val pokemonDetalle: StateFlow<Pokemon?> = _pokemonDetalle

    // Estado de carga y error compartidos
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Carga la lista inicial de Pokémon.
     */
    fun cargarPokemons() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            repositorio.getPokemonsList()
                .onSuccess { _pokemons.value = it }
                .onFailure { _error.value = it.message ?: "Error desconocido" }
            _cargando.value = false
        }
    }

    /**
     * Recarga la lista (para gesto de refresco).
     */
    fun refrescarLista() {
        cargarPokemons()
    }

    /**
     * Carga un Pokémon por ID para la pantalla de detalle.
     */
    fun cargarPokemonPorId(id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            repositorio.getPokemonById(id)
                .onSuccess { _pokemonDetalle.value = it }
                .onFailure { _error.value = it.message ?: "Error desconocido" }
            _cargando.value = false
        }
    }

    /**
     * Limpia el estado del detalle (útil si se sale de la pantalla).
     */
    fun limpiarDetalle() {
        _pokemonDetalle.value = null
    }
}
