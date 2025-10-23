package com.example.ejemploapi.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.data.network.PokeAPI
import com.example.ejemploapi.ui.theme.EjemploApiTheme
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Principal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PokemonListScreen(modifier: Modifier = Modifier) {
    val pokemons = remember { mutableStateListOf<Pokemon>() } // Lista mutable que se actualiza con los Pokémon obtenidos

    val retrofit = remember {
        // Inicializa Retrofit con la URL base de la PokeAPI y el convertidor Gson
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) { // Ejecuta la llamada a la API en un hilo de fondo (IO)
            val call = retrofit.create(PokeAPI::class.java).getPokemons().execute()
            val response = call.body()
            if (call.isSuccessful && response != null) {
                // Limpia y actualiza la lista con los resultados obtenidos
                pokemons.clear()
                pokemons.addAll(response.results)
            }
        }
    }

    // Lista vertical que muestra los Pokémon en pares
    LazyColumn(modifier = modifier.padding(16.dp)) {
        // Divide la lista en grupos de 2 elementos
        items(pokemons.chunked(2)) { parPokemon ->
            Row(modifier = Modifier.fillMaxWidth()) {
                // Itera sobre cada Pokémon del par y lo muestra en una columna
                parPokemon.forEach { pokemon ->
                    Column(
                        modifier = Modifier
                            .weight(1f) // Ocupa mitad de la fila
                            .padding(8.dp)
                    ) {
                        Button(onClick = {
                            // aquí debería de llevar a otra actividad con el pokemon
                        }) {
                            Text(
                                text = pokemon.getNombre(), // Muestra el nombre del Pokémon
                            )
                        }
                    }
                }
                // Si el par tiene solo un elemento, añade espacio para alinear
                if (parPokemon.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    EjemploApiTheme {
        PokemonListScreen()
    }
}