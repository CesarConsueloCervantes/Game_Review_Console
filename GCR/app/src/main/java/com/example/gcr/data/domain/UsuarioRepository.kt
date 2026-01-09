package com.example.gcr.data.domain

import com.example.gcr.data.models.Usuario
import com.google.gson.JsonObject
import retrofit2.Response

interface UsuarioRepository {
    suspend fun getUsuario(id: String): Response<Usuario>

    suspend fun subcription(id: String, body: Map<String, String>): Response<Unit>

    suspend fun register(body: JsonObject): Response<Usuario>

    suspend fun login(body: JsonObject): Response<Usuario>
}