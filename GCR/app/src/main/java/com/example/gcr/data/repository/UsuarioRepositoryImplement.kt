package com.example.gcr.data.repository

import com.example.gcr.data.domain.UsuarioRepository
import com.example.gcr.data.models.Usuario
import com.example.gcr.data.remote.RetrofitHelper
import com.google.gson.JsonObject
import retrofit2.Response
import javax.inject.Inject

class UsuarioRepositoryImplement @Inject constructor(): UsuarioRepository {
    override suspend fun getUsuario(id: String): Response<Usuario> {
        return RetrofitHelper.api.getUsuario(id)
    }

    override suspend fun subcription(
        id: String,
        body: Map<String, String>
    ): Response<Unit> {
        return RetrofitHelper.api.subcription(id, body)
    }

    override suspend fun register(body: JsonObject): Response<Usuario> {
        return RetrofitHelper.api.register(body)
    }

    override suspend fun login(body: JsonObject): Response<Usuario> {
        return RetrofitHelper.api.login(body)
    }
}