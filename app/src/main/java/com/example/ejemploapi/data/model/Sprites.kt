package com.example.ejemploapi.data.model

import com.google.gson.annotations.SerializedName

// Esta clase representa el objeto "sprites" que viene dentro del JSON de un Pokémon
class Sprites(
    @SerializedName("front_default") // Este campo del JSON se llama exactamente así
    val frontDefault: String? // Aquí guardamos la URL de la imagen principal del Pokémon
)
