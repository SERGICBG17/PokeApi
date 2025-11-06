package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ejemploapi.ui.viewModels.DatosViewModel

/**
 * Pantalla que muestra el nombre y la imagen de un Pokémon.
 * - Usa DatosViewModel para obtener los datos desde la URL
 * - Muestra solo el nombre y la imagen
 * - Incluye botón para volver atrás
 *
 * URL del Pokémon en la PokéAPI
 * Nombre del Pokémon
 * Acción al pulsar "Volver"
 * ViewModel que gestiona la carga del detalle
 */
@Composable
fun DatosDelPokemonScreen(
    url: String,
    nombre: String,
    onBack: () -> Unit,
    viewModel: DatosViewModel = viewModel()
) {
    val pokemon by viewModel.pokemonDetalle.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Cargar datos solo una vez
    LaunchedEffect(url) {
        viewModel.cargarDetallePokemon(url)
    }

    // Usar las funciones del ViewModel
    val id = viewModel.extraerIdDesdeUrl(url)
    val imagenUrl = viewModel.buildImageUrl(id)

    // Estado: cargando
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Estado: error
    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Error desconocido")
        }
        return
    }

    // Estado: datos cargados
    pokemon?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = imagenUrl,
                contentDescription = "Imagen de ${nombre}",//se podría quitar de recibir el nombre y ponet it.name pero lo dejo así
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a la lista")
            }
        }
    }
}
