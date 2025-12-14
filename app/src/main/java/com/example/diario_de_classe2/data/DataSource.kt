package com.example.diario_de_classe2.data

import android.content.Context
import kotlinx.coroutines.delay

class DataSource(private val context: Context) {
    suspend fun loadAluno(): List<Aluno> {
        delay(1000)
        return listOf(
            Aluno("Ana Clara", course = "Matemática", email = "teste1@gmail", password = "1234", photo = null),
            Aluno("Bruno Silva", course = "História", email = "teste2@gmail", password = "1234", photo = null),
            Aluno("Carla Souza", course = "Geografia", email = "teste3@gmail", password = "1234", photo = null),
        )
    }

    companion object {
        fun loadAluno() {
            TODO("Not yet implemented")
        }
    }
}