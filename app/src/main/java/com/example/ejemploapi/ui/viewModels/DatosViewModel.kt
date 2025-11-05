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
            _isLoading.value=true

            if(repository.getPokemonsList() == null){
                _errorMessage.value = "Error al obtener datos"
            }else{
                _pokemons.value = repository.getPokemonsList()
            }
            _isLoading.value = false
        }
    }

    /**
     * Carga el detalle de un Pokémon desde su URL.
     * Convierte la URL en ID y llama al repositorio.
     *
     * @param url URL completa del Pokémon en la PokéAPI
     */
    fun cargarDetallePokemon(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _pokemonDetalle.value = null

            val detalle = repository.getPokemonByUrl(url)

            if (detalle != null) {
                _pokemonDetalle.value = detalle
            } else {
                _errorMessage.value = "No se pudo cargar el detalle del Pokémon"
            }

            _isLoading.value = false
        }
    }

    /**
     * Construye la URL de imagen frontal del Pokémon usando su ID.
     * Ejemplo: id = 25 → https://.../pokemon/25.png
     */
    fun buildImageUrl(id: Unit): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }


}