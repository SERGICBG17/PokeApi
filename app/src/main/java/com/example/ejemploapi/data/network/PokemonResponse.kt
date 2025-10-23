package com.example.ejemploapi.data.network

import com.example.ejemploapi.data.model.Pokemon
import com.google.gson.annotations.SerializedName

data class PokemonResponse (
    @SerializedName("results") var results: ArrayList<Pokemon>
)