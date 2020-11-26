package com.example.android.developers.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(var movies: MutableList<Movie> = mutableListOf()) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    interface OnPosterClickListener {
        fun onPosterClick(position: Int)
    }

    interface OnReachEndListener {
        fun onReachEnd()
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int)
    }

    var onPosterClickListener: OnPosterClickListener? = null
    var onReachEndListener: OnReachEndListener? = null
    var onLongClickListener: OnLongClickListener? = null

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewSmallPoster =
            itemView.findViewById<ImageView>(R.id.image_view_small_poster)

        init {
            itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(position = adapterPosition)
            }

            itemView.setOnLongClickListener {
                onLongClickListener?.onLongClick(position = adapterPosition)
                true
            }
        }

        fun bind(position: Int) {
            val movie = movies[position]
            Picasso.get().load(movie.posterPath).into(imageViewSmallPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(position = position)
        if (position >= movies.size.minus(4) && movies.size >= 20) onReachEndListener?.onReachEnd()
    }

    override fun getItemCount(): Int = movies.size

    fun addMovie(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun clear() {
        this.movies.clear()
        notifyDataSetChanged()
    }
}