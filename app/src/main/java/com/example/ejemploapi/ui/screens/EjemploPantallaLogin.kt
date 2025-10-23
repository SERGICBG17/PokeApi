package com.example.ejemploapi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun PantallaLogin(){
    Column {
        TextoTitulo()
        BotonLogin()
    }
}

@Composable
fun BotonLogin() {
    Button(onClick = {}){
        Text(text = "Login")
    }
}

@Composable
fun TextoTitulo() {
    Text(text = "Pantalla Login",
        fontSize = 20.sp,
        color = Color.Blue)
}

@Preview
@Composable
fun PreviewPantallaLogin(){
    PantallaLogin()
}