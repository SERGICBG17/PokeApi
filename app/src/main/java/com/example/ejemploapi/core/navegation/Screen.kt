package com.example.ejemploapi.core.navegation

import kotlinx.serialization.Serializable

// Pantalla de login (formulario de acceso)
@Serializable
object Login

// Pantalla de registro (crear cuenta)
@Serializable
object Registro

// Pantalla de bienvenida o presentación
@Serializable
object Inicio

// Pantalla que muestra y permite editar los datos del usuario
@Serializable
object DatosDelPerfil

// Pantalla que muestra datos de una API pública (por ejemplo, Pokémon)
@Serializable
object LlamadaApiPokemons

// Pantalla que recibe un parámetro (por ejemplo, nombre y url)
@Serializable
data class DatosDelPokemon(val nombre: String, val url: String)

// si necesitamos pasar algo más pesado, un objeto, un array,
// lo hacemos pasando id y utilizando sharedPrefs