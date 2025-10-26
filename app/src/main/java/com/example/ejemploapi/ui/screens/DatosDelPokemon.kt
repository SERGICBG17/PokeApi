package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.network.PokeAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun DatosDelPokemonScreen(
    id: Int, // ID del Pokémon que queremos mostrar
    navController: NavController? // Controlador de navegación para poder volver atrás
) {
    // Variable para guardar el Pokémon que vamos a cargar
    var pokemon by remember { mutableStateOf<Pokemon?>(null) }

    // Variable para guardar un posible error
    var error by remember { mutableStateOf<String?>(null) }

    // Configuramos Retrofit para conectarnos a la API de Pokémon
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para convertir JSON
            .build()
    }

    // Este bloque se ejecuta cuando se abre la pantalla
    LaunchedEffect(true) {
        // Ejecutamos la llamada en un hilo de fondo
        withContext(Dispatchers.IO) {
            try {
                // Creamos la llamada a la API para obtener el Pokémon por ID
                val call = retrofit.create(PokeAPI::class.java).getPokemon(id).execute()

                // Si la llamada fue exitosa, guardamos el Pokémon
                if (call.isSuccessful) {
                    pokemon = call.body()
                } else {
                    // Si hubo error en la respuesta, lo guardamos
                    error = "Error al cargar el Pokémon"
                }
            } catch (e: Exception) {
                // Si hubo un error de red, lo guardamos
                error = "Error de red o sin conexión"
            }
        }
    }

    // Si hubo error, lo mostramos en pantalla
    if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = error ?: "Error desconocido")
        }
    }

    // Si el Pokémon se cargó correctamente, mostramos sus datos
    if (pokemon != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Espaciado interno
            horizontalAlignment = Alignment.CenterHorizontally // Centramos el contenido
        ) {
            // Mostramos el nombre del Pokémon
            Text(
                text = "Nombre: ${pokemon!!.getNombre()}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos

            // Mostramos el peso
            Text(text = "Peso: ${pokemon!!.weight}")

            // Mostramos la altura
            Text(text = "Altura: ${pokemon!!.height}")

            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes de la imagen

            // Mostramos la imagen del Pokémon usando Coil
            AsyncImage(
                model = pokemon!!.sprites?.frontDefault, // URL de la imagen
                contentDescription = "Imagen de ${pokemon!!.getNombre()}",
                modifier = Modifier.size(200.dp) // Tamaño de la imagen
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espacio antes del botón

            // Botón para volver a la lista de Pokémon
            Button(
                onClick = fun() {
                    // Volvemos a la pantalla anterior usando el controlador de navegación
                    if (navController != null) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth() // El botón ocupa todo el ancho
            ) {
                Text("Volver a la lista") // Texto del botón
            }
        }
    }
}