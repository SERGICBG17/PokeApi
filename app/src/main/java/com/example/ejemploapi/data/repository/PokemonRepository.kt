package com.example.ejemploapi.data.repository

import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio de datos para Pokémon.
 * - Centraliza la lógica de acceso a la red (Retrofit)
 * - Devuelve Result para manejar éxito/errores con claridad
 */
class PokemonRepository {
    private val api = RetrofitClient.pokeAPI

    /**
     * Obtiene la lista de Pokémon (los de la página inicial de la API).
     * Retorna Result<List<Pokemon>> para que la UI sepa si hubo error.
     */
    suspend fun getPokemonsList(): Result<List<Pokemon>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPokemons().execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body.results)
            } else {
                Result.failure(Exception("Error en la respuesta de la API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene el detalle de un Pokémon por su ID.
     * Centraliza la llamada de detalle que antes hacía la pantalla.
     */
    suspend fun getPokemonById(id: Int): Result<Pokemon> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPokemon(id).execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Error al cargar el Pokémon"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
