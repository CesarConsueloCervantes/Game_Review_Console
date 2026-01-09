package com.example.gcr.data.di

import com.example.gcr.data.repository.ConsoleRepositoryImplement
import com.example.gcr.data.repository.GameRepositoryImplement
import com.example.gcr.data.repository.UsuarioRepositoryImplement

object RepositoryProvider {
    val consoleRepo by lazy { ConsoleRepositoryImplement() }
    val gameRepo by lazy { GameRepositoryImplement() }
    val usuarioRepo by lazy { UsuarioRepositoryImplement() }
}