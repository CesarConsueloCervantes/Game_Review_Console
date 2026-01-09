package com.example.gcr

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gcr.ViewModel.AppViewModelFactory
import com.example.gcr.ViewModel.ConsoleViewModel
import com.example.gcr.ViewModel.GameViewModel
import com.example.gcr.ViewModel.UsuarioViewModel
import com.example.gcr.adapter.ConsoleAdapter
import com.example.gcr.adapter.GamesAdapter
import com.example.gcr.data.local.GCRDB
import com.example.gcr.databinding.HomeScreenFragmentBinding
import com.example.gcr.decoration.HorizontalItemDecoration

class HomeFragment : Fragment(){

    private var _binding: HomeScreenFragmentBinding? = null

    private val binding get() = _binding!!

    lateinit var consoleViewModel: ConsoleViewModel
    lateinit var gameViewModel: GameViewModel
    lateinit var usuarioViewModel: UsuarioViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //inicializamos los repositorios
        var repository = AppViewModelFactory()
        consoleViewModel = ViewModelProvider(requireActivity(), repository)[ConsoleViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity(), repository)[GameViewModel::class.java]
        usuarioViewModel = ViewModelProvider(requireActivity(), repository)[UsuarioViewModel::class.java]

        //inicializamos la base de datos
        val dbHelper = GCRDB(requireContext())
        val db = dbHelper.writableDatabase

        //obtenemos el id del usuario de la base de datos
        val cursor: Cursor = db.rawQuery("SELECT * FROM user LIMIT 1", null)
        cursor.moveToFirst()
        if (cursor.getCount() > 0){
            val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
            usuarioViewModel.getUsuario(id)
        }

        //obtenemos los valores iniciales
        consoleViewModel.getConsoles()
        gameViewModel.getFeatures()

        val title_featured = view.findViewById<TextView>(R.id.titleFeatured)

        //inicializamos los adaptadores
        val console_adapter = ConsoleAdapter(emptyList()) { console ->
            gameViewModel.getGamesByConsole(console._id)
            title_featured.text = console.console_name
        }

        val game_adapter = GamesAdapter(emptyList()) { game ->
            //Log.d("CLICK", "Click recibido")
            val bundle = Bundle().apply {
                putString("game", game.game)
                putString("game_id", game._id)
            }
            findNavController().navigate(R.id.action_HomeFragment_to_DetailFragment, bundle)
        }

        //asignamos los adaptadores a los recyclerViews
        binding.recyclerGames.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGames.adapter = game_adapter

        binding.filterRow.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filterRow.addItemDecoration(HorizontalItemDecoration(16))
        binding.filterRow.adapter = console_adapter

        //la busqueda por nombre
        var search_bar = view.findViewById<EditText>(R.id.searchBar)
        search_bar.addTextChangedListener { text ->
            //Log.d("HomeFragment", "Buscando: ${text.toString()}")
            if(text == null || text.toString().isEmpty()) {
                gameViewModel.getFeatures()
                title_featured.text = "Featured Games"
            } else {
                gameViewModel.getGamesSearch(text.toString())
                title_featured.text = "Search Results"
            }
        }

        //observamos los cambios en la listas y las actualizamos dinamicamente
        consoleViewModel.consoles.observe(viewLifecycleOwner){ lista ->
            //Log.d("HomeFragment", "Consolas recibidas: ${lista?.size}")
            console_adapter.updateList(lista)
        }

        gameViewModel.games.observe(viewLifecycleOwner){ lista ->
            //Log.d("HomeFragment", "Juegos recibidos: ${lista?.size}")
            game_adapter.updateList(lista)
        }

        //el boton para ir a la pantalla del usuario
        val userButton = view.findViewById<ImageButton>(R.id.searchIcon)
        userButton.setOnClickListener {
            if(usuarioViewModel.usuario.value?._id == null || usuarioViewModel.usuario.value?._id == ""){
                findNavController().navigate(R.id.AuthFragment)
            } else {
                findNavController().navigate(R.id.UserFragment)
            }
        }

        usuarioViewModel.usuario.observe(viewLifecycleOwner){ usuario ->
            if(usuario.image != null && usuario.image != "")
                Glide.with(requireContext()).load(usuario.image).into(userButton)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}