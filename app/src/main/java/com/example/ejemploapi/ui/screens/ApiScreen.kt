package com.example.ejemploapi.ui.screens

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
import com.example.ejemploapi.ui.viewModels.VistaViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Pantalla que muestra la lista de Pokémon.
 * Toda la lógica de datos ahora está en VistaViewModel/Repositorio.
 */
@Composable
fun ApiScreen(
    onPerfilClick: () -> Unit,
    onPokemonClick: (Int) -> Unit,
    viewModel: VistaViewModel = viewModel()
) {
    // Estados observados del ViewModel
    val pokemons by viewModel.pokemons.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    // Estado de refresco (para SwipeRefresh)
    val refrescando = remember { mutableStateOf(false) }

    // Carga inicial de datos
    LaunchedEffect(Unit) {
        viewModel.cargarPokemons()
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
            onClick = onPerfilClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Ir al perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            cargando && !refrescando.value -> {
                // Indicador de progreso mientras se cargan los datos (solo si no es refresh)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                // Mensaje de error claro
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error ?: "Error desconocido")
                }
            }

            else -> {
                // Envolvemos la lista con SwipeRefresh para permitir el gesto de refresco
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refrescando.value),
                    onRefresh = {
                        refrescando.value = true
                        viewModel.refrescarLista()
                    }
                ) {
                    // Sincroniza el estado "refrescando" con "cargando" del ViewModel
                    LaunchedEffect(cargando) {
                        if (!cargando) {
                            refrescando.value = false
                        }
                    }

                    // Lista de Pokémon en tarjetas
                    LazyColumn {
                        items(pokemons) { pokemon: Pokemon ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        // Extraemos el ID del Pokémon desde su URL (formato .../pokemon/{id}/)
                                        val url = pokemon.getUrl()
                                        val idTexto = url.trimEnd('/').split("/").last()
                                        val id = idTexto.toIntOrNull()
                                        if (id != null) {
                                            onPokemonClick(id)
                                        }
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
