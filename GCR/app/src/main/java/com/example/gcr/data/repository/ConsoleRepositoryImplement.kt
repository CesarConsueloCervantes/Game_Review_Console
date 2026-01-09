package com.example.gcr.data.repository

import com.example.gcr.data.domain.ConsoleRepository
import com.example.gcr.data.models.Console
import com.example.gcr.data.models.Game
import com.example.gcr.data.remote.ApiService
import com.example.gcr.data.remote.RetrofitHelper
import retrofit2.Response
import javax.inject.Inject

class ConsoleRepositoryImplement @Inject constructor(): ConsoleRepository {
    override suspend fun getConsoles(): Response<List<Console>> {
        return RetrofitHelper.api.getConsoles()
    }
}