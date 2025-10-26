package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    // Creamos una instancia de FirebaseAuth para acceder a la autenticación
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Función que intenta iniciar sesión con email y contraseña
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        // Comprobamos que el usuario haya escrito ambos campos
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Rellena todos los campos") // Si falta algo, mostramos mensaje
            return
        }

        // Usamos Firebase para intentar iniciar sesión con los datos
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si todo salió bien, devolvemos éxito
                    onResult(true, null)
                } else {
                    // Si falló (por ejemplo, datos incorrectos), mostramos mensaje de error
                    onResult(false, "Email o contraseña incorrectos")
                }
            }
    }
}

