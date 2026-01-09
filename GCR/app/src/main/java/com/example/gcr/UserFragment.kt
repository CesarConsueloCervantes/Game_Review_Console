package com.example.gcr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gcr.ViewModel.AppViewModelFactory
import com.example.gcr.ViewModel.UsuarioViewModel
import com.example.gcr.adapter.GamesAdapter
import com.example.gcr.adapter.ReviewsAdapter
import com.example.gcr.adapter.UserReviewsAdapter
import com.example.gcr.data.local.GCRDB
import com.example.gcr.data.models.User
import com.example.gcr.data.models.Usuario
import com.example.gcr.databinding.HomeScreenFragmentBinding
import com.example.gcr.databinding.UserScreenFragmentBinding
import com.example.gcr.decoration.VerticalItemDecoration

class UserFragment : Fragment(){
    private var _binding: UserScreenFragmentBinding? = null

    private val binding get() = _binding!!

    lateinit var usuarioViewModel: UsuarioViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var repository = AppViewModelFactory()
        usuarioViewModel = ViewModelProvider(requireActivity(), repository)[UsuarioViewModel::class.java]

        val usuario = usuarioViewModel.usuario.value

        val avatar = binding.avatar
        val name = binding.tvName
        val reviews_count = binding.tvReviewsCount
        val games_count = binding.tvGamesCount

        val reviewsAdapter = UserReviewsAdapter(usuario!!.reviews)

        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerReviews.addItemDecoration(VerticalItemDecoration(16))
        binding.recyclerReviews.adapter = reviewsAdapter

        //reviewsAdapter.updateList(usuario!!.reviews)

        val gamesAdapter =object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            usuario!!.games_followed_names
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.textSize = 25f // <-- tamaño del texto
                return view
            }
        }

        binding.listGames.adapter = gamesAdapter

        usuario.image.let{ image->
            if(image != null)
                Glide.with(requireContext()).load(image).into(avatar)
        }

        usuario.nombre.let { nombre ->
            name.text = nombre
        }

        usuario.reviews.let { reviews ->
            reviews_count.text = reviews.size.toString()
        }

        usuario.games_followed_names.let { games ->
            games_count.text = games.size.toString()
        }

        val reviews_button = binding.reviewsButton
        val games_button = binding.gamesButton

        reviews_button.setOnClickListener {
            binding.listGames.visibility = View.GONE
            binding.recyclerReviews.visibility = View.VISIBLE
        }

        games_button.setOnClickListener {
            binding.recyclerReviews.visibility = View.GONE
            binding.listGames.visibility = View.VISIBLE
        }

        binding.tvLogout.setOnClickListener {
            val dbHelper = GCRDB(requireContext())
            val db = dbHelper.writableDatabase
            dbHelper.onUpgrade(db, 1, 2)
            usuarioViewModel._usuario.value = Usuario("","","",emptyList(), emptyList())
            findNavController().navigate(R.id.HomeFragment)
        }

        binding.tvBack.setOnClickListener {
            findNavController().navigate(R.id.HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}