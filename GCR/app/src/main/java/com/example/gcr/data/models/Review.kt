package com.example.gcr.data.models

class Review (
    val review: String,
    val comment: String,
    val usuario: User,
    val game_version: GameVersion
)

data class User(
    val nombre: String,
    val image: String
)

data class GameVersion(
    val game_name: String,
    val console_name:String
)