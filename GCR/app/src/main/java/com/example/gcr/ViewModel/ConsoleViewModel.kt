package com.example.gcr.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcr.data.di.RepositoryProvider
import com.example.gcr.data.domain.ConsoleRepository
import com.example.gcr.data.models.Console
import com.example.gcr.data.models.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConsoleViewModel(
    private val consoleRepository: ConsoleRepository = RepositoryProvider.consoleRepo
) : ViewModel() {
    val _consoles = MutableLiveData<List<Console>>()
    val consoles: LiveData<List<Console>> get() = _consoles

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    fun getConsoles(){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try{
                val response = consoleRepository.getConsoles()

                if(!response.isSuccessful){
                    _error.value = "Error al cargar Consolas: ${response}"
                    Log.d("ConsoleViewModel", "${_error.value}")
                    _consoles.value = emptyList()
                } else {
                    val consoles = response.body()
                    Log.d("ConsoleViewModel", "${consoles?.size}")
                    _consoles.value = consoles?: emptyList()
                }

            } catch(e: Exception){
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("ConsoleViewModel", "${_error.value}")
                _consoles.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}