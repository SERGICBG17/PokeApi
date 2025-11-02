package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ejemploapi.ui.viewModels.DatosViewModel

/**
 * Pantalla que muestra el detalle de un Pokémon.
 * - Usa DatosViewModel para obtener los datos
 * - Muestra nombre, peso, altura e imagen
 * - Incluye botón para volver atrás
 */
@Composable
fun DatosDelPokemonScreen(
    id: Int,
    navController: NavController?,
    viewModel: DatosViewModel = viewModel()
) {
    // Observamos los estados del ViewModel
    val pokemon by viewModel.pokemonDetalle.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Al entrar en la pantalla, cargamos el Pokémon por ID
    LaunchedEffect(id) {
        viewModel.cargarPokemonPorId(id)
    }

    // Mostramos un spinner si está cargando
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Si hay error, lo mostramos
    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Error desconocido")
        }
        return
    }

    // Si el Pokémon se cargó correctamente, mostramos sus datos
    pokemon?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nombre: ${it.getNombre()}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Peso: ${it.weight}")
            Text(text = "Altura: ${it.height}")

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = it.sprites?.frontDefault,
                contentDescription = "Imagen de ${it.getNombre()}",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a la lista")
            }
        }
    }
}
