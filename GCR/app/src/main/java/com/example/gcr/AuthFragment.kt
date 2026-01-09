package com.example.gcr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gcr.ViewModel.AppViewModelFactory
import com.example.gcr.ViewModel.UsuarioViewModel
import com.example.gcr.data.local.GCRDB
import com.example.gcr.databinding.AuthScreenFragmentBinding
import com.google.gson.JsonObject

class AuthFragment: Fragment() {

    private  var _binding: AuthScreenFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var usuarioViewModel: UsuarioViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AuthScreenFragmentBinding.inflate(inflater, container, false)

        binding.loginScreen.tvRegister.setOnClickListener {
            binding.loginScreen.root.visibility = View.GONE
            binding.registerScreen.root.visibility = View.VISIBLE
        }

        binding.registerScreen.btnLogin.setOnClickListener {
            binding.loginScreen.root.visibility = View.VISIBLE
            binding.registerScreen.root.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //inicializamos el repositorio
        var repository = AppViewModelFactory()
        usuarioViewModel = ViewModelProvider(requireActivity(), repository)[UsuarioViewModel::class.java]

        //TODO: login screen
        val user_name_login = binding.loginScreen.etUsername
        val password_login = binding.loginScreen.etPassword
        val login_button = binding.loginScreen.btnLogin

        login_button.setOnClickListener {
            val username = user_name_login.text.toString()
            val password = password_login.text.toString()

            val body = JsonObject().apply{
                addProperty("nombre", username)
                addProperty("password", password)
            }
            usuarioViewModel.login(body, requireContext())
        }

        //TODO: register screen
        val user_name_register = binding.registerScreen.etUsername
        val password_register = binding.registerScreen.etPassword
        val confirm_password_register = binding.registerScreen.etConfirmPassword
        val register_button = binding.registerScreen.btnRegister

        register_button.setOnClickListener {
            val username = user_name_register.text.toString()
            val password = password_register.text.toString()
            val confirm_password = confirm_password_register.text.toString()

            if(password != confirm_password){
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Las contraseñas no coinciden")
                    .setPositiveButton("Aceptar", null)
                    .show()
                return@setOnClickListener
            } else {
                val body = JsonObject().apply{
                    addProperty("nombre", username)
                    addProperty("password", password)
                }
                usuarioViewModel.register(body, requireContext())
            }
        }

        usuarioViewModel.usuario.observe(viewLifecycleOwner){ usuario ->
            //usuario._id.isEmpty()
            if(usuario._id != ""){
                val dbHelper = GCRDB(requireContext())
                val db = dbHelper.writableDatabase
                dbHelper.insertUser(db, usuario._id)
                findNavController().navigate(R.id.UserFragment)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}