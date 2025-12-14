package com.example.diario_de_classe2.ui.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diario_de_classe2.data.LocalUserRepository
import com.example.diario_de_classe2.data.api.ApiService
import com.example.diario_de_classe2.data.api.LoginRequest
import com.example.diario_de_classe2.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitClient.apiService
    private val localUserRepository = LocalUserRepository(application)
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var login by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    init {
    }

    fun resetLogin() {
        reset()
    }

    fun updateLogin(login: String) {
        this.login = login
        _uiState.update { it.copy(email = login, errorEmailOrPassword = false) }
    }

    fun updatePassword(password: String) {
        this.password = password
        _uiState.update { it.copy(password = password, errorEmailOrPassword = false) }
    }

    fun login() {
        if (login.isBlank() || password.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    errorEmailOrPassword = true,
                    labelLogin = "Preencha todos os campos",
                    labelPassword = "Preencha todos os campos",
                    loginSucess = false,
                    isLoading = false
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorEmailOrPassword = false) }

        viewModelScope.launch {
            try {
                // Tenta fazer login via API primeiro
                val request = LoginRequest(email = login, password = password)
                val response = apiService.login(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            loginSucess = true,
                            errorEmailOrPassword = false,
                            labelLogin = "",
                            labelPassword = "",
                            isLoading = false
                        )
                    }
                } else {
                    tryLocalLogin()
                }
            } catch (e: Exception) {
                tryLocalLogin()
            }
        }
    }
    
    private fun tryLocalLogin() {
        if (localUserRepository.validateLogin(login, password)) {
            _uiState.update { currentState ->
                currentState.copy(
                    loginSucess = true,
                    errorEmailOrPassword = false,
                    labelLogin = "",
                    labelPassword = "",
                    isLoading = false
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    errorEmailOrPassword = true,
                    labelLogin = "Email ou senha inválidos",
                    labelPassword = "Email ou senha inválidos",
                    loginSucess = false,
                    isLoading = false
                )
            }
        }
    }

    fun reset() {
        _uiState.update {
            currentState -> currentState.copy(
                email = "",
                password = "",
                errorEmailOrPassword = false,
                labelLogin = "",
                labelPassword = "",
                loginSucess = false,
                isLoading = false
            )
        }
    }
}