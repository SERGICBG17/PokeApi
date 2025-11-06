package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploapi.data.model.Pokemon
import com.example.ejemploapi.ui.viewModels.DatosViewModel

/**
 * Pantalla principal que muestra la lista de Pokémon.
 * - Muestra rueda de carga mientras se obtienen los datos
 * - Permite navegar al perfil y al detalle de cada Pokémon
 */
@Composable
fun ListadoDePokemons(
    onPerfilClick: () -> Unit,
    onPokemonClick: (String, String) -> Unit,
    viewModel: DatosViewModel = viewModel()
) {
    val pokemons by viewModel.pokemons.observeAsState(emptyList())

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

        ListaPokemons(
            pokemons = pokemons,
            onPokemonClick = onPokemonClick,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun ListaPokemons(
    pokemons: List<Pokemon>,
    onPokemonClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pokemons.size) { index ->
            val pokemon = pokemons[index]
            Button(
                onClick = { onPokemonClick(pokemon.url, pokemon.name) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF0F0F0), // grisáceo claro
                        contentColor = Color(0xFF333333)   // texto gris oscuro
                )
            ) {
                //lo añado para alinearlo a la izquierda
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = pokemon.name)
                }
            }
        }
    }
}
