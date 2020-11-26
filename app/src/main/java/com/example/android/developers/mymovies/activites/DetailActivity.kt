package com.example.android.developers.mymovies.activites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.adapters.ReviewAdapter
import com.example.android.developers.mymovies.adapters.TrailerAdapter
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.viewmodel.MainViewModel
import com.example.android.developers.mymovies.utils.JsonUtils
import com.example.android.developers.mymovies.utils.NetworkUtils
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.movie_info.*
import java.util.*
import kotlin.Exception

class DetailActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel
    private var id: Int = -1
    private lateinit var movie: Movie

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var trailerAdapter: TrailerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        super.hideActionBar()
        lang = Locale.getDefault().language
        setDataActivity(movie = init())
        super.clickedNavigationView(navigationView = navigation_view_detail)
        clickedFloatButton()
        initRecyclerViewReview()
        initRecyclerViewTrailer()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_main -> startActivity(Intent(this, MainActivity::class.java))
            R.id.item_favourite -> startActivity(Intent(this, FavouriteActivity::class.java))
        }
        return true
    }


    private fun init(): Movie {
        viewModel = ViewModelProviders.of(this@DetailActivity)[MainViewModel::class.java]
        id = intent.getIntExtra(MainActivity.INTENT_SAVE_MOVIE_ID, -1)
        movie = try {
            viewModel.getMovieById(id = id)
        } catch (e: Exception){
            try {
                viewModel.getFavouriteMovieById(id = id)?.getMovie()
                    ?: throw Exception()
            } catch (e: Exception) {
                super.showSnackBar(
                    text = "Не удалось загрузить фильм",
                    coordinatorLayout = detail_coordinator_layout
                )
            }
        } as Movie

        return movie
    }

    private fun setDataActivity(movie: Movie) {
        Picasso.get().load(movie.bigPosterPath).placeholder(R.drawable.cinema)
            .into(image_view_big_poster)
        text_view_title.text = movie.title
        text_view_original_title.text = movie.originalTitle
        text_view_rating.text = movie.voteAverage.toString()
        text_view_release_date.text = movie.releaseDate
        text_view_overview.text = movie.overview
        if (viewModel.getFavouriteMovieById(id) != null) {
            float_button_add_favourite.setImageResource(R.drawable.ic_baseline_clear_24)
        } else {
            float_button_add_favourite.setImageResource(R.drawable.star_positive)
        }
    }


    private fun clickedFloatButton() {
        float_button_add_favourite.setOnClickListener {
            val favouriteMovie = viewModel.getFavouriteMovieById(id = id)
            if (favouriteMovie == null) {
                viewModel.insertFavouriteMovie(favouriteMovie = FavouriteMovie(movie = movie))
                float_button_add_favourite.setImageResource(R.drawable.ic_baseline_clear_24)
                super.showSnackBar(
                    text = getString(R.string.add_fovourite_snackbar_text),
                    coordinatorLayout = detail_coordinator_layout
                )
            } else {
                viewModel.deleteFavouriteMovie(favouriteMovie = FavouriteMovie(movie = movie))
                float_button_add_favourite.setImageResource(R.drawable.star_positive)
                super.showSnackBar(
                    text = getString(R.string.remove_fovourite_snackbar_text),
                    coordinatorLayout = detail_coordinator_layout
                )

            }

        }
    }

    private fun initRecyclerViewTrailer() {
        // получаем список с данными для трейлера
        val resultTrailers =
            JsonUtils.getVideosFromJson(
                NetworkUtils.getJsonFromNetworkToVideos(
                    id = movie.id,
                    lang = lang
                )
            )
        trailerAdapter = TrailerAdapter(trailers = resultTrailers)

        recycler_view_detail_trailers.layoutManager = LinearLayoutManager(this@DetailActivity)
        recycler_view_detail_trailers.adapter = trailerAdapter
        trailerAdapter.onPlayClickListener = object : TrailerAdapter.OnPlayClickListener {
            override fun playVideo(key: String) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(key)))
            }
        }
        trailerAdapter.notifyDataSetChanged()

    }

    private fun initRecyclerViewReview() {
        // получаем список с данными для рецензий
        val resultReviews =
            JsonUtils.getReviewsFromJson(
                NetworkUtils.getJsonFromNetworkToReviews(
                    id = movie.id,
                    lang = lang
                )
            )
        reviewAdapter = ReviewAdapter(reviews = resultReviews)

        recycler_view_detail_reviews.layoutManager = LinearLayoutManager(this@DetailActivity)
        recycler_view_detail_reviews.adapter = reviewAdapter
        reviewAdapter.notifyDataSetChanged()
    }

    companion object {
        private lateinit var lang: String
    }


}