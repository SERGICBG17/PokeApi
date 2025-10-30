package com.example.ejemploapi.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val pokeAPI : PokeAPI = retrofit.create(PokeAPI::class.java)
}