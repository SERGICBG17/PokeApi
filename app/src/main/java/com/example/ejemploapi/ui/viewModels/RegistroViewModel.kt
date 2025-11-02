package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemploapi.data.repository.AuthRepository
import kotlinx.coroutines.launch
class RegistroViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // Campos de entrada
    private val _email = MutableLiveData<String?>(null)
    val email: LiveData<String?> = _email

    private val _password = MutableLiveData<String?>(null)
    val password: LiveData<String?> = _password

    // Estados de la UI
    private val _errorMessage = MutableLiveData(false) // true si hubo error
    val errorMessage: LiveData<Boolean> = _errorMessage

    private val _isRegistrer = MutableLiveData(false) // true mientras se procesa el registro
    val isRegistrer: LiveData<Boolean> = _isRegistrer

    private val _isRegistrerOk = MutableLiveData(false) // true si el registro fue exitoso
    val isRegistrerOk: LiveData<Boolean> = _isRegistrerOk

    // Actualizar email desde la UI
    fun onEmailChanged(email: String) {
        _email.value = email
    }

    // Actualizar password desde la UI
    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onRegistrer() {
        val emailValue = _email.value ?: ""
        val passwordValue = _password.value ?: ""

        if (emailValue.isBlank() || passwordValue.isBlank()) {
            _errorMessage.value = true
            return
        }

        viewModelScope.launch {
            _isRegistrer.value = true

            val result = authRepository.register(emailValue, passwordValue)

            result.onSuccess {
                // Si el registro fue exitoso, marcamos OK
                _isRegistrerOk.value = true
            }.onFailure {
                // Si falló, marcamos error
                _errorMessage.value = true
            }

            _isRegistrer.value = false
        }
    }
}


