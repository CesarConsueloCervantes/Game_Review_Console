package com.example.gcr.data.models

class Usuario (
    val _id: String,
    val nombre: String,
    val image: String,
    val games_followed_names: List<String>,
    val reviews: List<Review>
)