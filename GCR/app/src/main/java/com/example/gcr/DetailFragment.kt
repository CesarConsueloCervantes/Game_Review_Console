package com.example.gcr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gcr.ViewModel.AppViewModelFactory
import com.example.gcr.ViewModel.GameViewModel
import com.example.gcr.ViewModel.UsuarioViewModel
import com.example.gcr.adapter.ConsoleAdapter
import com.example.gcr.adapter.ReviewsAdapter
import com.example.gcr.data.models.Console
import com.example.gcr.data.models.Game
import com.example.gcr.databinding.DetailScreenFragmentBinding
import com.example.gcr.decoration.HorizontalItemDecoration
import com.example.gcr.decoration.VerticalItemDecoration
import com.google.gson.JsonObject

class DetailFragment : Fragment() {

    private var _binding: DetailScreenFragmentBinding? = null

    private val binding get() = _binding!!

    lateinit var gameViewModel: GameViewModel

    lateinit var game_id_version: MutableLiveData<String>
    lateinit var usuarioViewModel: UsuarioViewModel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setFragmentResultListener("review") { _, bundle ->
//            val review_score = bundle.getString("review_score")
//            val comment = bundle.getString("comment")
//
//            Log.d("DetailFragment", "review_score: ${review_score}")
//
//
//            val json = JsonObject().apply{
//                addProperty("review", review_score)
//                addProperty("comment", comment)
//                addProperty("usuario", "691d077176ad3204bf8d1c0b")
//                addProperty("game_version", game_id_version.value)
//            }
//            gameViewModel.postReview(json)
//            gameViewModel.getReviewsByGameVercion(game_id_version.value.toString())
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var initialized = false

        var repository = AppViewModelFactory()
        gameViewModel = ViewModelProvider(requireActivity(), repository)[GameViewModel::class.java]
        usuarioViewModel = ViewModelProvider(requireActivity(), repository)[UsuarioViewModel::class.java]


        //se obtienen los argumentos desde HomeFragment
        val bundle = arguments
        val id_game = bundle?.getString("game").toString()
        val game_id = bundle?.getString("game_id").toString()
        game_id_version = MutableLiveData<String>(game_id)

        //son mausquerramientas que nos ayudaran mas adelante
        val list_consoles = mutableListOf<Console>()
        val list_games = mutableMapOf<String, Game>()

        val game_image = view.findViewById<ImageView>(R.id.ivCover)
        val game_title = view.findViewById<TextView>(R.id.tvGameTitle)
        val game_console = view.findViewById<TextView>(R.id.txtPlatform)
        val game_review_rate = view.findViewById<TextView>(R.id.tvScorePill)


        gameViewModel.getGameVersions(id_game)
        gameViewModel.getReviewsByGameVercion(game_id)

        val console_adapter = ConsoleAdapter(emptyList()) { cursor->
            game_id_version.value = cursor._id
            //Log.d("DetailFragment", "cursor: ${cursor._id}")
        }

        val review_adapter = ReviewsAdapter(emptyList())

        binding.filterRow.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filterRow.addItemDecoration(HorizontalItemDecoration(16))
        binding.filterRow.adapter = console_adapter

        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerReviews.addItemDecoration(VerticalItemDecoration(16))
        binding.recyclerReviews.adapter = review_adapter


        gameViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (!initialized && loading == false) {
                game_id_version.value = game_id
                initialized = true
            }
        }

        gameViewModel.games.observe(viewLifecycleOwner){ lista ->
            list_games.clear()
            list_consoles.clear()
            //Log.d("DetailFragment", "lista: ${lista.size}")
            lista.forEach { game->
                val console = Console(game._id, game.console_name)
                list_consoles.add(console)
                list_games.put(game._id, game)
            }
            console_adapter.updateList(list_consoles)
        }

        gameViewModel.reviews.observe(viewLifecycleOwner) { lista ->
            review_adapter.updateList(lista)
        }

        game_id_version.observe(viewLifecycleOwner){ id ->
            //Log.d("DetailFragment", "id: ${id}")
            val game = list_games[id]
            //Log.d("DetailFragment", "list_games: ${list_games.size}")
            if (game?.image_vercion != "")
                Glide.with(requireContext()).load(game?.image_vercion).into(game_image)

            game?.game_name.let { name ->
                game_title.text = name
            }

            game?.console_name.let { name ->
                game_console.text = name
            }

            game?.review_rate.let { rating ->
                when(rating){
                    "Very Positive" -> game_review_rate.setTextAppearance(R.style.very_positive_style)
                    "Positive" -> game_review_rate.setTextAppearance(R.style.positive_style)
                    "Mixed" -> game_review_rate.setTextAppearance(R.style.mixed_style)
                    "Mostly Negative" -> game_review_rate.setTextAppearance(R.style.mostly_negative_style)
                    "Negative" -> game_review_rate.setTextAppearance(R.style.negative_style)
                    else -> game_review_rate.setTextAppearance(R.style.none_style)
                }
                game_review_rate.text = rating
            }
            gameViewModel.getReviewsByGameVercion(id)
        }

        binding.btnWriteReview.setOnClickListener{
            setFragmentResultListener("review") { _, bundle ->
                val review_score = bundle.getString("review_score")
                val comment = bundle.getString("comment")

                Log.d("DetailFragment", "review_score: ${review_score}")


                val json = JsonObject().apply{
                    addProperty("review", review_score)
                    addProperty("comment", comment)
                    addProperty("usuario", usuarioViewModel.usuario.value?._id)
                    addProperty("game_version", game_id_version.value)
                }
                gameViewModel.postReview(json, game_id_version.value)
                game_id_version.value = game_id_version.value
            }
            WriteReviewDialog().show(parentFragmentManager, "WriteReviewDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}