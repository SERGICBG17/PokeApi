package com.example.ejemploapi.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse (
    @SerializedName("results") var results: ArrayList<Pokemon>
)