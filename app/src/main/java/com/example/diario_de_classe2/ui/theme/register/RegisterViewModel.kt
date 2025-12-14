package com.example.diario_de_classe2.ui.register

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diario_de_classe2.data.DataSource
import com.example.diario_de_classe2.data.LocalUserRepository
import com.example.diario_de_classe2.data.Aluno
import com.example.diario_de_classe2.data.api.ApiService
import com.example.diario_de_classe2.data.api.RegisterRequest
import com.example.diario_de_classe2.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSource = DataSource(application)
    private val apiService: ApiService = RetrofitClient.apiService
    private val localUserRepository = LocalUserRepository(application)
    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun carregarUsuarios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val usuarios = dataSource.loadAluno()

            _uiState.update {
                it.copy(
                    users = usuarios,
                    isLoading = false
                )
            }
        }
    }

    fun onNomeChange(newValue: String) {
        _uiState.update { it.copy(nome = newValue, registerError = false) }
    }

    fun onLoginChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, registerError = false) }
    }

    fun onSenhaChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, registerError = false) }
    }

    fun onFotoChange(newUri: Uri?) {
        _uiState.update { it.copy(photo = newUri) }
    }

    fun register() {
        val state = _uiState.value
        
        if (!validateRegister(state.email, state.password, state.nome)) {
            _uiState.update { it.copy(registerError = true, isLoading = false) }
            return
        }

        _uiState.update { it.copy(isLoading = true, registerError = false) }

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    name = state.nome,
                    email = state.email,
                    password = state.password,
                    course = "Undeclared"
                )
                
                val response = apiService.register(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.update {
                        it.copy(
                            loginSucess = true,
                            registerError = false,
                            nome = "",
                            email = "",
                            password = "",
                            photo = null,
                            isLoading = false
                        )
                    }
                } else {
                    // Se a API falhar, salva localmente
                    saveUserLocally(state)
                }
            } catch (e: Exception) {
                // Se houver erro de conexão, salva localmente
                saveUserLocally(state)
            }
        }
    }
    
    private fun saveUserLocally(state: RegisterUIState) {
        val newUser = Aluno(
            name = state.nome,
            email = state.email,
            password = state.password,
            photo = state.photo,
            course = "Undeclared"
        )
        
        localUserRepository.saveUser(newUser)
        
        _uiState.update {
            it.copy(
                loginSucess = true,
                registerError = false,
                nome = "",
                email = "",
                password = "",
                photo = null,
                isLoading = false
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "",
                password = "",
                nome = "",
                photo = null,
                registerError = false,
                loginSucess = false,
                isLoading = false
            )
        }
    }

    private fun validateRegister(
        email: String,
        password: String,
        nome: String
    ): Boolean {
        if (email.isBlank() || password.isBlank() || nome.isBlank()) {
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if (password.length < 6) {
            return false
        }
        return true
    }
}