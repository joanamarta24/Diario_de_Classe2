package com.example.diario_de_classe.data.api

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val course: String = "Undeclared"
)

