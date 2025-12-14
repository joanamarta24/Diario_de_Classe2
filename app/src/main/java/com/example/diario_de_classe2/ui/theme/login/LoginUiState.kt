package com.example.diario_de_classe2.ui.login

data class LoginUiState (
    val email: String = "",
    val password: String = "",
    val errorEmailOrPassword: Boolean = false,
    val labelLogin: String? = "",
    val labelPassword: String? = "",
    val loginSucess: Boolean = false,
    val isLoading: Boolean = false
)