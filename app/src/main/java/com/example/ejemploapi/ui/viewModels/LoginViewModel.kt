package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login.
 * Aquí centralizamos toda la lógica de autenticación con Firebase,
 * de forma que la pantalla solo se preocupe de mostrar la UI.
 */
class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository() // Repositorio que habla con Firebase

    // Estados que la UI puede observar
    private val _cargando = MutableStateFlow(false) // true mientras se hace login
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null) // mensaje de error si algo falla
    val error: StateFlow<String?> = _error

    private val _loginExitoso = MutableStateFlow(false) // true si el login fue correcto
    val loginExitoso: StateFlow<Boolean> = _loginExitoso

    /**
     * Intenta iniciar sesión con email y contraseña.
     * - Valida que no estén vacíos
     * - Llama al repositorio
     * - Comprueba si el email está verificado
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Rellena todos los campos"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            authRepository.login(email, password)
                .onSuccess { user ->
                    if (user.isEmailVerified) {
                        _loginExitoso.value = true
                    } else {
                        _error.value = "Verifica tu email antes de iniciar sesión"
                        authRepository.logout()
                    }
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Error al iniciar sesión"
                }
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}


