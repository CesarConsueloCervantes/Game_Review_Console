package com.example.gcr.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcr.data.di.RepositoryProvider
import com.example.gcr.data.domain.GameRepository
import com.example.gcr.data.models.Game
import com.example.gcr.data.models.Review
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository = RepositoryProvider.gameRepo
): ViewModel() {

    val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews


    val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    val _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    fun getFeatures(){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try{
                val response = gameRepository.getFeatures()

                if(!response.isSuccessful){
                    _error.value = "Error al cargar juegos: ${response}"
                    Log.d("GameViewModel", "${_error.value}")
                    _games.value = emptyList()
                } else {
                    val games = response.body()
                    Log.d("GameViewModel", "Games size: ${games?.size}")
                    _games.value = games?: emptyList()
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
                _games.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun getGamesSearch(search: String){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try{
                val response = gameRepository.getGamesSearch(search)

                if(!response.isSuccessful){
                    _error.value = "Error al cargar juegos: ${response}"
                    Log.d("GameViewModel", "${_error.value}")
                    _games.value = emptyList()
                } else {
                    val games = response.body()
                    Log.d("GameViewModel", "Games size: ${games?.size}")
                    _games.value = games?: emptyList()
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
                _games.value = emptyList()
            }finally {
                _loading.value = false
            }
        }
    }

    fun getGameVersions(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {

                val response = gameRepository.getGameVersions(id)

                if (!response.isSuccessful()) {
                    _error.value = "Error al cargar juegos: ${response}"
                    Log.d("GameViewModel", "${_error.value}")
                    _games.value = emptyList()
                } else {
                    val games = response.body()
                    //val json = games.toString()
                    Log.d("GameViewModel", "Games size: ${games?.size}")
                    //Log.d("GameViewModel", "Games: ${json}")
                    _games.value = games ?: emptyList()
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
                _games.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun getGamesByConsole(id: String){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try{
                val response = gameRepository.getGamesByConsole(id)

                if(!response.isSuccessful){
                    _error.value = "Error al cargar Games: ${response}"
                    Log.d("GameViewModel", "${_error.value}")
                    _games.value = emptyList()
                } else {
                    val games = response.body()
                    Log.d("GameViewModel", "${games?.size}")
                    _games.value = games?: emptyList()
                }

            } catch (e: Exception){
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
                _games.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun getReviewsByGameVercion(id: String){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try{

                val response = gameRepository.getReviewsByGameVercion(id)

                if(!response.isSuccessful){
                    _error.value = "Error al cargar Reviews: ${response}"
                    Log.d("GameViewModel", "${_error.value}")
                    _reviews.value = emptyList()
                } else {
                    val reviews = response.body()
                    Log.d("GameViewModel", "Reviews size: ${reviews?.size}")
                    _reviews.value = reviews?: emptyList()
                }
            } catch (e: Exception){
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
                _reviews.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun postReview(review: JsonObject, id: String){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = gameRepository.postReview(review)

                if (response.isSuccessful) {
                    _error.value = "Review enviada"
                    Log.d("GameViewModel", "${_error.value}")
                    getReviewsByGameVercion(id)
                } else {
                    _error.value = "Error al enviar Review: ${response.errorBody()}"
                    Log.d("GameViewModel", "${_error.value}")
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("GameViewModel", "${_error.value}")
            } finally {
                _loading.value = false
            }
        }
    }
}