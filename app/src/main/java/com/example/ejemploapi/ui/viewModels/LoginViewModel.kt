package com.example.ejemploapi.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val authRepository = AuthRepository()
    private val _email = MutableLiveData<String?>(null)
    val email: LiveData<String?> = _email

    private val _password = MutableLiveData<String?>(null)
    val password: LiveData<String?> = _password

    private val _errorMessage = MutableLiveData(false)
    val errorMessage : LiveData<Boolean> = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isLoadiginOk = MutableLiveData(false)
    val isLoadiginOk : LiveData<Boolean> = _isLoadiginOk

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onLogin(){
        val emailValue = _email.value ?: ""
        val passwordValue = _password.value ?: ""

        if (emailValue.isBlank() || passwordValue.isBlank()) {
            //poner todas las comprobaciones
            _errorMessage.value = true
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            val result = authRepository.login(emailValue, passwordValue)

            result.onSuccess {
                _isLoadiginOk.value = true
            }.onFailure {
                _errorMessage.value = true
            }

            _isLoading.value = false
        }

    }

}


