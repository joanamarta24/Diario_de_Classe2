package com.example.diario_de_classe2.data

import java.net.URI

data class Usuario(
    val name: String,
    val email: String,
    val password: String,
    val image: URI? = null
)
