package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Registro.
 * Se encarga de crear el usuario en Firebase y enviar el email de verificación.
 */
class RegistroViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // Estados observables por la UI
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    /**
     * Intenta registrar un usuario con email y contraseña.
     * - Valida que los campos no estén vacíos
     * - Valida que la contraseña tenga al menos 6 caracteres
     * - Llama al repositorio para crear el usuario
     * - Envía email de verificación
     */
    fun registrar(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Rellena todos los campos"
            return
        }
        if (password.length < 6) {
            _error.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            authRepository.register(email, password)
                .onSuccess { user ->
                    authRepository.sendEmailVerification(user)
                        .onSuccess {
                            _registroExitoso.value = true
                            authRepository.logout() // cerramos sesión hasta que verifique
                        }
                        .onFailure { e ->
                            _error.value = e.message ?: "Error al enviar verificación"
                        }
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Error al registrar usuario"
                }
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}


