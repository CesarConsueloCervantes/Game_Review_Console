package com.example.gcr.data.remote

import com.example.gcr.data.models.Console
import com.example.gcr.data.models.Game
import com.example.gcr.data.models.Review
import com.example.gcr.data.models.Usuario
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //TODO: Authenficacion
    //registra un usuario
    @POST("auth/register")
    suspend fun register(@Body body: JsonObject): Response<Usuario>

    //logea un usuario
    @POST("auth/login")
    suspend fun login(@Body body: JsonObject): Response<Usuario>

    //TODO: Consolas
    //obtiene todas las consolas
    @GET("console")
    suspend fun getConsoles(): Response<List<Console>>

    //obtiene los juegos por version de consola
    @GET("console/{id}/games")
    suspend fun getGamesByConsole(@Path("id") id: String): Response<List<Game>>

    //TODO: Usuarios
    //obtinene al usuario
    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: String): Response<Usuario>

//    @POST("usuarios")
//    suspend fun postUsuario(@Body usuario: Usuario): Usuario

    //agrega una juego seguido
    @POST("usuarios/subscription/{id}")
    suspend fun subcription(@Path("id") id: String, @Body body: Map<String, String>): Response<Unit>

    //TODO: Games
    //obtiene los juegos favoritos
    @GET("features")
    suspend fun getFeatures(): Response<List<Game>>

    //obtiene los juegos buscados
    @GET("search/{search}")
    suspend fun getGamesSearch(@Path("search") search: String): Response<List<Game>>

    //obtiene los detalles de un juego
    @GET("game/{id}/version")
    suspend fun getGameVersions(@Path("id") id: String): Response<List<Game>>

    @GET("game/version/{id}")
    suspend fun getVercion(@Path("id") id: String): Response<Game>

    //obtiene las reviews de una version de un juego
    @GET("game/version/{id}/reviews")
    suspend fun getReviewsByGameVercion(@Path("id") id: String): Response<List<Review>>

    //TODO: Reviews
    //crea una review
    @POST("review")
    suspend fun postReview(@Body body: JsonObject): Response<Unit>

//    {
//        "review": "mixed", //not_recommended, recommended, mixed
//        "comment": "puede estar mejor",
//        "usuario": "691d077176ad3204bf8d1c0b",
//        "game_version": "6923e50f2a1f8592e86723c5"
//    }
}