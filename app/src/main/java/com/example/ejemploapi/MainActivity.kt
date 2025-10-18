package com.example.ejemploapi

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
import com.example.ejemploapi.ui.theme.EjemploApiTheme
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        enableEdgeToEdge()
        setContent {
            EjemploApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonListScreen(
                        retrofit = retrofit,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun PokemonListScreen(retrofit: Retrofit, modifier: Modifier = Modifier) {
        val pokemons = remember { mutableStateListOf<Pokemon>() }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                val call = retrofit.create(PokeAPI::class.java).getPokemons().execute()
                val response = call.body()
                if (call.isSuccessful && response != null) {
                    pokemons.clear()
                    pokemons.addAll(response.results)
                }
            }
        }

        LazyColumn(modifier = modifier.padding(16.dp)) {
            items(pokemons) { pokemon ->
                Text(
                    text = pokemon.getNombre(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewPokemonList() {
        EjemploApiTheme {
            PokemonListScreen(retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            )
        }
    }
}