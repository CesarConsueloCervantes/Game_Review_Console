package com.example.gcr.data.repository

import com.example.gcr.data.domain.GameRepository
import com.example.gcr.data.models.Game
import com.example.gcr.data.models.Review
import com.example.gcr.data.remote.RetrofitHelper
import com.google.gson.JsonObject
import okhttp3.Request
import retrofit2.Response
import javax.inject.Inject

class GameRepositoryImplement @Inject constructor(): GameRepository {
    override suspend fun getFeatures(): Response<List<Game>> {
        return RetrofitHelper.api.getFeatures()
    }

    override suspend fun getGamesSearch(search: String): Response<List<Game>> {
        return RetrofitHelper.api.getGamesSearch(search)
    }

    override suspend fun getGameVersions(id: String): Response<List<Game>> {
        return RetrofitHelper.api.getGameVersions(id)
    }

    override suspend fun getGamesByConsole(id: String): Response<List<Game>> {
        return RetrofitHelper.api.getGamesByConsole(id)
    }

    override suspend fun getReviewsByGameVercion(id: String): Response<List<Review>> {
        return RetrofitHelper.api.getReviewsByGameVercion(id)
    }

    override suspend fun postReview(review: JsonObject): Response<Unit> {
        return RetrofitHelper.api.postReview(review)
    }
}