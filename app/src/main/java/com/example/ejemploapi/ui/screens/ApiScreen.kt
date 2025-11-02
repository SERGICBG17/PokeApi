package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.ui.viewModels.DatosViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

/**
 * Pantalla principal que muestra la lista de Pokémon.
 * - Permite refrescar con gesto de swipe
 * - Muestra rueda de carga mientras se obtienen los datos
 * - Permite navegar al perfil y al detalle de cada Pokémon
 */
@Composable
fun ApiScreen(
    onPerfilClick: () -> Unit,
    onPokemonClick: (Int) -> Unit,
    viewModel: DatosViewModel = viewModel()
) {
    val pokemons by viewModel.pokemons.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Cargar datos al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarPokemons()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = onPerfilClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SwipeRefresh envuelve la lista y permite recargar
        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing = isLoading),
            onRefresh = { viewModel.cargarPokemons() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "Error desconocido")
                }

                else -> LazyColumn {
                    items(pokemons) { pokemon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    val id = pokemon.getUrl().trimEnd('/').split("/").last().toInt()
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
