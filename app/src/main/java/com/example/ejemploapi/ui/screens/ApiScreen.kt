package com.example.ejemploapi.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.network.PokeAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ApiScreen(
    onPerfilClick: () -> Unit,   // Se llama al pulsar el botón de perfil
    onPokemonClick: (Int) -> Unit // Se llama al pulsar un Pokémon (navegar al detalle)
) {
    // Lista mutable que la UI observa; cuando cambian sus datos, se redibuja
    val pokemons = remember { mutableStateListOf<Pokemon>() }

    // Estados de carga y error para mostrar feedback al usuario
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Configuración de Retrofit: base URL y conversor JSON (Gson)
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos
            .build()
    }

    // Al entrar en la pantalla, disparamos la carga de datos
    LaunchedEffect(Unit) {
        // Cambiamos al hilo para evitar NetworkOnMainThreadException
        withContext(Dispatchers.IO) {
            try {
                // Creamos un cliente de Retrofit a partir de la interfaz PokeAPI
                val service = retrofit.create(PokeAPI::class.java)
                // Hacemos la petición a la API de forma síncrona con .execute()
                val call = service.getPokemons().execute()
                // Obtenemos solo el cuerpo de la respuesta (el JSON convertido a objeto)
                val response = call.body()

                // Si el HTTP fue correcto y hay cuerpo, procesamos la lista
                if (call.isSuccessful && response != null && response.results.isNotEmpty()) {
                    // Actualizamos la lista observada por la UI
                    pokemons.clear()
                    pokemons.addAll(response.results)
                    error = null
                } else {
                    // Distinguimos casos para dar un mensaje de error claro
                    error = when {
                        response == null -> "Respuesta vacía de la API. Revisa el modelo PokemonResponse."
                        response.results.isEmpty() -> "La API respondió, pero la lista está vacía."
                        else -> "Error HTTP: ${call.code()} - ${call.message()}"
                    }
                }
            } catch (e: Exception) {
                // Mostramos el error real y lo dejamos en Logcat para depurar
                error = "Fallo de red o parseo: ${e.message ?: "desconocido"}"
                Log.e("ApiScreen", "Error al obtener datos", e)
            } finally {
                // Pase lo que pase, ya no estamos cargando
                cargando = false
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {

        // Título centrado
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Botón para ir al perfil
        Button(
            onClick = fun() { onPerfilClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Ir al perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estado de carga, error o lista
        when {
            cargando -> {
                // Indicador de progreso mientras se cargan los datos
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                // Mensaje de error claro; ya arriba se elabora el detalle
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error ?: "Error desconocido")
                }
            }

            else -> {
                // Lista de Pokémon en tarjetas
                LazyColumn {
                    items(pokemons) { pokemon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    // Extraemos el ID del Pokémon desde su URL (formato .../pokemon/{id}/)
                                    val url = pokemon.getUrl()
                                    val idTexto = url.trimEnd('/').split("/").last()
                                    val id = idTexto.toInt()
                                    onPokemonClick(id)
                                }
                        ) {
                            Text(
                                text = pokemon.getNombre(),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

