package com.example.ejemploapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PokeAPI {
    @Headers("Accept: application/json")
    // Método para obtener todos los pokemon
    @GET("pokemon")
    fun getPokemons(): Call<PokemonResponse>

    // Método para obtener una pokemon por su ID
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<Pokemon>
}