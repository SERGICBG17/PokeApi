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

        // Pantalla de datos del usuario
        composable<Datos> {
            DatosScreen(
                onLogout = {
                    // Al cerrar sesión volvemos a la pantalla de inicio
                    navController.navigate(Inicio) {
                        popUpTo(Inicio) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de lista de pokemons
        composable<Api> {
            ApiScreen(
                onPerfilClick = { navController.navigate(Datos) },
                onPokemonClick = { id -> navController.navigate(Vista(id)) // Cuando pulsamos un Pokémon vamos a la pantalla de detalle
                }
            )
        }


        // Definimos una ruta de navegación para mostrar la pantalla de detalle de un Pokémon
        composable<Vista> { entradaPila ->
            // Extraemos los argumentos que se pasaron a esta ruta usando la clase Vista
            // Vista es una clase que tú defines con una propiedad 'id' como Int
            val args = entradaPila.toRoute<Vista>()
            // Accedemos directamente al ID del Pokémon desde los argumentos
            val id = args.id
            // Llamamos al composable DatosDelPokemonScreen, pasándole el ID del Pokémon
            // También pasamos el navController para que el botón de "volver" dentro de esa pantalla funcione correctamente
            DatosDelPokemonScreen(id = id, navController = navController)
        }
    }
}
