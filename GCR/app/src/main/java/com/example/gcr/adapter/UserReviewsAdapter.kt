package com.example.gcr.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gcr.R
import com.example.gcr.adapter.ConsoleAdapter.ConsoleViewHolder
import com.example.gcr.data.models.Review

class UserReviewsAdapter(
    private var reviews: List<Review>,
): RecyclerView.Adapter<UserReviewsAdapter.UserReviewViewHolder>() {

    class UserReviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val console_name = itemView.findViewById<TextView>(R.id.txtConsole)
        val game_name = itemView.findViewById<TextView>(R.id.txtGameVercion)
        val ic_icon = itemView.findViewById<ImageView>(R.id.icIcon)
        val review_score = itemView.findViewById<TextView>(R.id.reviewScore)

        fun bind(review: Review) {
            review.game_version.console_name.let { console ->
                console_name.text = console
            }

            review.game_version.game_name.let { game ->
                game_name.text = game
            }

            review.review.let { review ->
                when(review){
                    "recommended" -> ic_icon.setImageResource(R.drawable.ic_like)
                    "mixed" -> ic_icon.setImageResource(R.drawable.ic_mixed)
                    "not_recommended" -> ic_icon.setImageResource(R.drawable.ic_dislike)
                }
                review_score.text = review.replaceFirstChar{it.uppercaseChar()}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_review, parent, false)
        return UserReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        if(position < reviews.size){
            holder.bind(reviews[position])
        } else {
            Log.w("fuera de rango","$position")
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun updateList(newList: List<Review>) {
        reviews = newList
        notifyDataSetChanged()
    }
}