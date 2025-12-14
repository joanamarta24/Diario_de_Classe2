package com.example.diario_de_classe.data

import android.net.Uri


data class Aluno(
    val name: String,
    val email: String,
    val password: String,
    val course: String,
    val photo: Uri? = null
)
