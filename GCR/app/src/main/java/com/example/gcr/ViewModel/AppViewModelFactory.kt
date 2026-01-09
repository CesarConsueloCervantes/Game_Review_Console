package com.example.gcr.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gcr.data.di.RepositoryProvider

class AppViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass) {

            ConsoleViewModel::class.java ->
                ConsoleViewModel(RepositoryProvider.consoleRepo) as T

            GameViewModel::class.java ->
                GameViewModel(RepositoryProvider.gameRepo) as T

            UsuarioViewModel::class.java ->
                UsuarioViewModel(RepositoryProvider.usuarioRepo) as T

            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

