package com.example.gcr.data.domain

import com.example.gcr.data.models.Game
import com.example.gcr.data.models.Review
import com.google.gson.JsonObject
import retrofit2.Response

interface GameRepository {
    suspend fun getFeatures(): Response<List<Game>>

    suspend fun getGamesSearch(search: String): Response<List<Game>>

    suspend fun getGameVersions(id: String): Response<List<Game>>

    suspend fun getGamesByConsole(id: String): Response<List<Game>>

    suspend fun getReviewsByGameVercion(id: String): Response<List<Review>>

    suspend fun postReview(review: JsonObject): Response<Unit>
}