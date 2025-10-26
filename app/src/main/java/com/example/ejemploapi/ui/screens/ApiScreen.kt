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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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

    // Estado adicional para controlar si el usuario está refrescando manualmente con el gesto
    val refrescando = remember { mutableStateOf(false) }

    // Scope para lanzar corrutinas desde eventos como el gesto de refresco
    val scope = rememberCoroutineScope()

    // Configuración de Retrofit: base URL y conversor JSON (Gson)
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos
            .build()
    }

    // Función que carga los datos desde la API (se puede reutilizar en el gesto de refresco)
    suspend fun cargarPokemons() {
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
                // Pase lo que pase, ya no estamos cargando ni refrescando
                cargando = false
                refrescando.value = false
            }
        }
    }

    // Al entrar en la pantalla, disparamos la carga de datos
    LaunchedEffect(Unit) {
        cargarPokemons()
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
                // Envolvemos la lista con SwipeRefresh para permitir el gesto de deslizar hacia abajo
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refrescando.value),
                    onRefresh = {
                        // Cuando el usuario desliza hacia abajo, recargamos los datos
                        refrescando.value = true
                        cargando = true
                        scope.launch {
                            cargarPokemons()
                        }
                    }
                ) {
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
}
