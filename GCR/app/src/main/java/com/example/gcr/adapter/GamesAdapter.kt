package com.example.gcr.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gcr.R
import com.example.gcr.adapter.ConsoleAdapter.ConsoleViewHolder
import com.example.gcr.data.models.Game

class GamesAdapter(
    private var games: List<Game>,
    private val onItemClick: (Game) -> Unit
): RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {
    class GameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val game_image = itemView.findViewById<ImageView>(R.id.cardImage)
        private val game_title = itemView.findViewById<TextView>(R.id.txtTitle)
        private val game_rating = itemView.findViewById<TextView>(R.id.txtRating)
        private val game_reviews = itemView.findViewById<TextView>(R.id.txtReviews)
        private val game_console = itemView.findViewById<TextView>(R.id.txtConsole)

        fun bind(game: Game, onItemClick: (Game) -> Unit) {
            Glide.with(itemView.context).load(game.image_vercion).into(game_image)

            game.game_name.let { name ->
                game_title.text = name
            }

            game.review_rate.let { rating ->
                when(rating){
                    "Very Positive" -> game_rating.setTextAppearance(R.style.very_positive_style)
                    "Positive" -> game_rating.setTextAppearance(R.style.positive_style)
                    "Mixed" -> game_rating.setTextAppearance(R.style.mixed_style)
                    "Mostly Negative" -> game_rating.setTextAppearance(R.style.mostly_negative_style)
                    "Negative" -> game_rating.setTextAppearance(R.style.negative_style)
                    else -> game_rating.setTextAppearance(R.style.none_style)
                }
                game_rating.text = rating
            }

            game.console_name.let { console ->
                game_console.text = console
            }

            itemView.setOnClickListener {
                onItemClick(game)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        if(position < games.size){
            holder.bind(games[position], onItemClick)
        } else {
            Log.w("fuera de rango","$position")
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun updateList(newList: List<Game>) {
        games = newList
        notifyDataSetChanged()
    }
}