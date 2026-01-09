package com.example.gcr.data.domain

import com.example.gcr.data.models.Console
import com.example.gcr.data.models.Game
import retrofit2.Response

interface ConsoleRepository {
    suspend fun getConsoles(): Response<List<Console>>
}