package com.example.android.developers.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.data.dataclasses.Review
import com.example.android.developers.mymovies.data.dataclasses.Trailer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrailerAdapter(var trailers: List<Trailer>) :
    RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    interface OnPlayClickListener {
        fun playVideo(key: String)
    }

    var onPlayClickListener: OnPlayClickListener? = null

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewName = itemView.findViewById<TextView>(R.id.text_view_name_of_video)
        private val floatButton =
            itemView.findViewById<FloatingActionButton>(R.id.float_button_play)

        init {
            floatButton
                .setOnClickListener { onPlayClickListener?.playVideo(key = trailers[adapterPosition].key) }
        }

        fun bind(position: Int) {
            textViewName.text = trailers[position].name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.trailer_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(position = position)
    }

    override fun getItemCount(): Int = trailers.size
}