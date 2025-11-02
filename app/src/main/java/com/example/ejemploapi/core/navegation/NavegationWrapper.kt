package com.example.ejemploapi.core.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ejemploapi.ui.screens.*

@Composable
fun NavigationWrapper() {
    // Este es el controlador de navegación, el que nos permite movernos entre pantallas
    val navController = rememberNavController()

    // Aquí definimos todas las pantallas y cuál es la inicial
    NavHost(navController = navController, startDestination = Inicio) {

        // Pantalla de inicio
        composable<Inicio> {
            InicioScreen(
                onLoginClick = { navController.navigate(Login) },
                onRegistroClick = { navController.navigate(Registro) }
            )
        }

        // Pantalla de login
        composable<Login> {
            LoginScreen(
                // Después de iniciar sesión vamos a la lista de Pokémon
                onLoginSuccess = { navController.navigate(Api) }
            )
        }

        // Pantalla de registro
        composable<Registro> {
            RegistroScreen(
                // Después de registrarse vamos a la pantalla de login
                onRegistroSuccess = { navController.navigate(Login) }
            )
        }

        composable<Datos> {
            DatosScreen(
                onLogout = {
                    navController.navigate(Inicio) {
                        popUpTo(Inicio) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de lista de pokemons
        composable<Api> {
            ApiScreen(
                // Al pulsar "Cerrar sesión", volvemos a Inicio y limpiamos el stack
                onPerfilClick = {
                    navController.navigate(Datos) {
                        popUpTo(Inicio) { inclusive = true }
                    }
                },
                // Cuando pulsamos un Pokémon vamos a la pantalla de detalle
                onPokemonClick = { id ->
                    navController.navigate(Vista(id))
                }
            )
        }

        // Pantalla de detalle de un Pokémon
        composable<Vista> { entradaPila ->
            val args = entradaPila.toRoute<Vista>()
            val id = args.id
            DatosDelPokemonScreen(id = id, navController = navController)
        }
    }
}
