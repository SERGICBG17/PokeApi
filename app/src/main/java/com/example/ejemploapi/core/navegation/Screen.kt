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
object Datos

// Pantalla que muestra datos de una API pública (por ejemplo, Pokémon)
@Serializable
object Api

// Pantalla que recibe un parámetro (por ejemplo, nombre)
@Serializable
data class Vista(val id: Int)

// si necesitamos pasar algo más pesado, un objeto, un array,
// lo hacemos pasando id y utilizando sharedPrefs