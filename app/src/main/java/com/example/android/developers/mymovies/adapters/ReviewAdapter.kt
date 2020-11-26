package com.example.android.developers.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.data.dataclasses.Review

class ReviewAdapter(var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewAuthor = itemView.findViewById<TextView>(R.id.text_view_author)
        private val textViewContent = itemView.findViewById<TextView>(R.id.text_view_content)

        fun bind(position: Int) {
            textViewAuthor.text = reviews[position].author
            textViewContent.text = reviews[position].content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.review_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(position - position)
    }

    override fun getItemCount(): Int = reviews.size
}