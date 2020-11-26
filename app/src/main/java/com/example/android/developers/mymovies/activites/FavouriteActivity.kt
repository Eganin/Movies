package com.example.android.developers.mymovies.activites

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.adapters.MovieAdapter
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_faivorite.*
import kotlinx.android.synthetic.main.favourite.*

class FavouriteActivity : BaseActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faivorite)
        super.hideActionBar()
        initRecyclerView()
        super.clickedNavigationView(navigationView = navigation_view_favourite)
    }

    override fun onStart() {
        super.onStart()
        progress_bar_loading_favourite_movie.visibility = View.INVISIBLE
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter()
        recycler_view_favourite.layoutManager = GridLayoutManager(this@FavouriteActivity, getColumnCount())
        recycler_view_favourite.adapter = adapter
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        // выполняется каждый раз при изменении данных
        viewModel.favouriteMovies?.observe(this, {
            val movies = mutableListOf<Movie>()
            for (movie in it) {
                movies.add(movie.getMovie())
            }
            adapter.movies = movies
            if (movies.size == 0) {
                super.showSnackBar(
                    text = getString(R.string.not_found_favourite_movies),
                    coordinatorLayout = favourite_coordinator_layout
                )
            }

            // реализация интерфейса при клике
            adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
                override fun onPosterClick(position: Int) {
                    progress_bar_loading_favourite_movie.visibility = View.VISIBLE
                    val movie = adapter.movies[position]
                    val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                    intent.putExtra(MainActivity.INTENT_SAVE_MOVIE_ID, movie.id)

                    startActivity(intent)
                }
            }
            // реализация интерфейса при долгом нажатии
            adapter.onLongClickListener = object : MovieAdapter.OnLongClickListener {
                override fun onLongClick(position: Int) {
                    removeTask(favouriteMovie = movies[position].getFavouriteMovie())
                }
            }

            adapter.notifyDataSetChanged()
        })
    }

    private fun removeTask(favouriteMovie: FavouriteMovie) {
        println(favouriteMovie.id)
        viewModel.deleteFavouriteMovie(favouriteMovie)
        adapter.notifyDataSetChanged()
    }

    private fun getColumnCount(): Int {
        // вычесляем кол-во колонок для GridLayout
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width  = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (width / 185 > 2) width / 185 else 2
    }


}