package com.example.diario_de_classe.data

import java.net.URI

data class Usuario(
    val name: String,
    val email: String,
    val password: String,
    val image: URI? = null
)
