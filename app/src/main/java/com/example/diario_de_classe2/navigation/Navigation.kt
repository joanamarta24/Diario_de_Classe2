package com.example.diario_de_classe2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diario_de_classe2.data.Aluno
import com.example.diario_de_classe2.ui.login.LoginScreen
import com.example.diario_de_classe2.ui.register.RegisterScreen
import com.example.diario_de_classe2.ui.theme.AlunoListScreen


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object StudentsList : Screen("students_list")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = false }
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Screen.StudentsList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.StudentsList.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StudentsList.route) {
            // Lista de alunos mockados
            val alunos = remember {
                listOf(
                    Aluno(
                        name = "Ana Clara Silva",
                        course = "Matemática",
                        email = "ana.clara@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Bruno Silva Santos",
                        course = "História",
                        email = "bruno.silva@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Carla Souza Oliveira",
                        course = "Geografia",
                        email = "carla.souza@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Daniel Costa Lima",
                        course = "Física",
                        email = "daniel.costa@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Elena Ferreira",
                        course = "Química",
                        email = "elena.ferreira@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Felipe Rodrigues",
                        course = "Biologia",
                        email = "felipe.rodrigues@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Gabriela Alves",
                        course = "Português",
                        email = "gabriela.alves@email.com",
                        password = "",
                        photo = null
                    ),
                    Aluno(
                        name = "Henrique Martins",
                        course = "Inglês",
                        email = "henrique.martins@email.com",
                        password = "",
                        photo = null
                    ),
                )
            }
            AlunoListScreen(alunos = alunos)
        }
    }
}

