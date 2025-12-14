package com.example.diario_de_classe.data.api

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserResponse? = null
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val course: String
)

