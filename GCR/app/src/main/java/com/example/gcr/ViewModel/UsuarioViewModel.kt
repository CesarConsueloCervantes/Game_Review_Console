package com.example.gcr.ViewModel

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcr.data.di.RepositoryProvider
import com.example.gcr.data.domain.UsuarioRepository
import com.example.gcr.data.local.GCRDB
import com.example.gcr.data.models.Usuario
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val usuarioRepository: UsuarioRepository = RepositoryProvider.usuarioRepo
): ViewModel() {

    val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> get() = _usuarios

    val _usuario = MutableLiveData<Usuario>()
    val usuario: LiveData<Usuario> get() = _usuario

    val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    val _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    fun getUsuario(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {

                val response = usuarioRepository.getUsuario(id)

                if (!response.isSuccessful) {
                    _error.value = "Error al cargar usuario: ${response}"
                    Log.d("UsuarioViewModel", "${_error.value}")
                    _usuario.value = Usuario("","","",emptyList(), emptyList())
                } else {
                    val usuario = response.body()
                    Log.d("UsuarioViewModel", "Usuario: ${usuario}")
                    _usuario.value = usuario ?: Usuario("","","",emptyList(), emptyList())
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("UsuarioViewModel", "${_error.value}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(body: JsonObject, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {

                val response = usuarioRepository.register(body)

                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("UsuarioViewModel", "Usuario: ${usuario}")
                    getUsuario(usuario!!._id)

                } else{
                    _error.value = "Error al registrar usuario: ${response}"
                    Log.d("UsuarioViewModel", "${_error.value}")
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Error al registrar usuario")
                        .setPositiveButton("Aceptar", null)
                        .show()
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("UsuarioViewModel", "${_error.value}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun login(body: JsonObject, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {

                val response = usuarioRepository.login(body)

                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("UsuarioViewModel", "Usuario: ${usuario}")
                    getUsuario(usuario!!._id)

                } else {
                    _error.value = "Error al iniciar sesión: ${response}"
                    Log.d("UsuarioViewModel", "${_error.value}")
                    _usuario.value = Usuario("","","",emptyList(), emptyList())
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Usuario o contraseña incorrectos")
                        .setPositiveButton("Aceptar", null)
                        .show()
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                Log.d("UsuarioViewModel", "${_error.value}")
            } finally {
                _loading.value = false
            }
        }
    }
}