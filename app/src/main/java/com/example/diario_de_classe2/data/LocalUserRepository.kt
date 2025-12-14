package com.example.diario_de_classe2.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalUserRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val usersKey = "local_users"
    
    // Usuário padrão para login
    val defaultUser = Aluno(
        name = "Usuário Padrão",
        email = "admin@admin.com",
        password = "admin123",
        course = "Administração",
        photo = null
    )
    
    init {
        // Inicializa com o usuário padrão se não existir nenhum
        if (getAllUsers().isEmpty()) {
            saveUser(defaultUser)
        }
    }
    
    fun saveUser(user: Aluno) {
        val users = getAllUsers().toMutableList()
        // Verifica se o email já existe
        val existingIndex = users.indexOfFirst { it.email == user.email }
        if (existingIndex >= 0) {
            users[existingIndex] = user
        } else {
            users.add(user)
        }
        val json = gson.toJson(users)
        prefs.edit().putString(usersKey, json).apply()
    }
    
    fun getUserByEmail(email: String): Aluno? {
        return getAllUsers().find { it.email == email }
    }
    
    fun getAllUsers(): List<Aluno> {
        val json = prefs.getString(usersKey, null) ?: return emptyList()
        val type = object : TypeToken<List<Aluno>>() {}.type
        return try {
            gson.fromJson<List<Aluno>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun validateLogin(email: String, password: String): Boolean {
        val user = getUserByEmail(email)
        return user != null && user.password == password
    }
}

