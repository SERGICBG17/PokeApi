package com.example.ejemploapi.data.repository

import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.network.RetrofitClient
import retrofit2.await

/**
 * Repositorio que encapsula la lógica de acceso a datos de Pokémon desde la PokeAPI.
 *
 * Este repositorio se encarga de:
 * - Obtener la lista de Pokémon disponibles.
 * - Obtener los datos de un Pokémon específico por su ID.
 *
 * Utiliza Retrofit a través de [RetrofitClient.pokeAPI] y expone funciones suspend
 * para ser usadas desde ViewModels con corrutinas.
 */
class PokemonRepository {

    // Instancia de la interfaz PokeAPI
    private val api = RetrofitClient.pokeAPI

    /**
     * Obtiene la lista de Pokémon desde la API.
     * Devuelve una lista de objetos [Pokemon] con nombre y URL.
     * Si ocurre un error, devuelve una lista vacía.
     */
    suspend fun getPokemonsList(): List<Pokemon> {
        try {
           val response = api.getPokemons().await() // Llamada suspend a la API
           return response.results
        } catch (e: Exception) {
            return emptyList() // En caso de error, devuelvo lista vacía
        }
    }

    /**
     * Obtiene los datos de un Pokémon a partir de su URL.
     * Convierte la URL en ID y llama al método getPokemon(id).
     *
     * @param url URL completa del Pokémon en la PokéAPI
     * @return Objeto [Pokemon] si la llamada es exitosa, o null si falla
     */
    suspend fun getPokemonByUrl(url: String): Pokemon? {
        return try {
            val id = extraerIdDesdeUrl(url)
            api.getPokemon(id).await()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extrae el ID numérico desde una URL con formato conocido.
     * Ejemplo: "https://pokeapi.co/api/v2/pokemon/25/" → 25
     */
    private fun extraerIdDesdeUrl(url: String): Int {
        return url.trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull() ?: -1
    }

}
