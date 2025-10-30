package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ejemploapi.ui.viewModels.VistaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Pantalla de detalle de un Pokémon.
 * - Ya no crea Retrofit ni hace llamadas directas: usa VistaViewModel.
 * - Mantiene el botón de volver que utiliza el NavController.
 */
@Composable
fun DatosDelPokemonScreen(
    id: Int,
    navController: NavController?,
    viewModel: VistaViewModel = viewModel()
) {
    val pokemon by viewModel.pokemonDetalle.collectAsState()
    val error by viewModel.error.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    // Carga el detalle al entrar con el ID
    LaunchedEffect(id) {
        viewModel.cargarPokemonPorId(id)
    }

    when {
        cargando -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "Error desconocido")
            }
        }

        pokemon != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nombre del Pokémon
                Text(
                    text = "Nombre: ${pokemon!!.getNombre()}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Peso y altura
                Text(text = "Peso: ${pokemon!!.weight}")
                Text(text = "Altura: ${pokemon!!.height}")

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del Pokémon usando Coil
                AsyncImage(
                    model = pokemon!!.sprites?.frontDefault,
                    contentDescription = "Imagen de ${pokemon!!.getNombre()}",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón para volver
                Button(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a la lista")
                }
            }
        }

        else -> {
            // Estado vacío si aún no hay datos ni error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Cargando datos...")
            }
        }
    }
}