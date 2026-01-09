package com.example.gcr.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gcr.R
import com.example.gcr.adapter.GamesAdapter.GameViewHolder
import com.example.gcr.data.models.Review
import kotlin.text.replaceFirstChar

class ReviewsAdapter(
    private var reviews: List<Review>,
): RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {


    class ReviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val usuario_icon = itemView.findViewById<ImageView>(R.id.iconUsuario)
        val usuario_txt = itemView.findViewById<TextView>(R.id.txtUsuario)
        val ic_icon = itemView.findViewById<ImageView>(R.id.icIcon)
        val review_score = itemView.findViewById<TextView>(R.id.reviewScore)
        val review_comment = itemView.findViewById<TextView>(R.id.review)
        val show_more = itemView.findViewById<LinearLayout>(R.id.btnShowMore)


        fun bind(review: Review) {
            if (review.usuario.image != null)
                Glide.with(itemView.context).load(review.usuario.image).into(usuario_icon)

            review.usuario.nombre.let { name ->
                usuario_txt.text = name
            }

            review.review.let { review ->
                when(review){
                    "recommended" -> ic_icon.setImageResource(R.drawable.ic_like)
                    "mixed" -> ic_icon.setImageResource(R.drawable.ic_mixed)
                    "not_recommended" -> ic_icon.setImageResource(R.drawable.ic_dislike)
                }
                review_score.text = review.replaceFirstChar{it.uppercaseChar()}
            }

            review.comment.let { comment ->
                review_comment.text = comment
            }

            review_comment.post {
                val layout = review_comment.layout ?: return@post
                val isTruncated = layout.lineCount > review_comment.maxLines

                show_more.visibility = if (isTruncated) View.VISIBLE else View.GONE
            }

            review_comment.setOnClickListener{
                if(review_comment.visibility == View.VISIBLE){
                    review_comment.maxLines = 99
                    show_more.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
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
