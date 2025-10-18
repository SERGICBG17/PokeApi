package com.example.ejemploapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejemploapi.ui.theme.EjemploApiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var retrofit: Retrofit
    private var texto: String = "Pruebas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        texto = obtenerDatos(retrofit)

        enableEdgeToEdge()
        setContent {
            EjemploApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = texto,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun obtenerDatos(retrofit: Retrofit): String {
        var texto = ""
        CoroutineScope(Dispatchers.IO).launch {
            val call = retrofit.create(PokeAPI::class.java).getPokemons().execute()
            val pokemons = call.body()
            if (call.isSuccessful) {
                texto = pokemons?.results?.get(1)?.getNombre().toString()
            } else {
                texto = "Ha habido un error"
            }
        }
        Thread.sleep(1000)
        return texto
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {

        Text(
            text = "Hello $name!",
            modifier = modifier
        )

    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        EjemploApiTheme {
            Greeting("Android")
        }
    }
}