package com.example.diario_de_classe2.ui.register

import android.net.Uri
import com.example.diario_de_classe2.data.Aluno

data class RegisterUIState(
    val email: String = "",
    val password: String = "",
    val nome: String = "",
    val photo: Uri? = null,
    val users: List<Aluno> = emptyList(),
    val isLoading: Boolean = false,
    val registerError: Boolean = false,
    val loginSucess: Boolean = false
)
