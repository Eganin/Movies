package com.example.android.developers.mymovies.activites

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.developers.mymovies.R
import com.example.android.developers.mymovies.adapters.MovieAdapter
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.viewmodel.MainViewModel
import com.example.android.developers.mymovies.utils.JsonUtils
import com.example.android.developers.mymovies.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main.*
import org.json.JSONObject
import java.util.*

class MainActivity : BaseActivity(), LoaderManager.LoaderCallbacks<JSONObject> {
    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var loaderManager: LoaderManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.hideActionBar()
        // получаем язык устройства
        lang = Locale.getDefault().language
        viewModel = ViewModelProviders.of(this@MainActivity)[MainViewModel::class.java]
        //  отвечает за все загрузки в приложении
        loaderManager = LoaderManager.getInstance(this@MainActivity)
        initRecyclerView()
        onClickTextView()
        super.clickedNavigationView(navigationView = navigation_view_main)

        val moviesFromLiveData = viewModel.movies
        moviesFromLiveData?.observe(this@MainActivity, {
            if (page == 1) adapter.movies = it as MutableList<Movie>

        })
    }

    // создание загрузчика
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<JSONObject> {
        val jsonLoader = NetworkUtils.JsonLoader(context = this@MainActivity, bundle = args)
        jsonLoader.onStartLoadingListener =
            object : NetworkUtils.JsonLoader.OnStartLoadingListener {
                override fun onStartLoading() {
                    progress_bar_loading.visibility = View.VISIBLE
                    isLoading = true
                }
            }
        return jsonLoader
    }

    override fun onLoadFinished(loader: Loader<JSONObject>, data: JSONObject?) {
        val movies = JsonUtils.getMoviesFromJSON(jsonObject = data) as MutableList<Movie>
        if (movies.isNotEmpty()) {
            if (page == 1) {
                // удаляем предыдущие данные т.к появились новые
                viewModel.deleteAllMovies()
                adapter.clear()
            }

            for (movie in movies) {
                viewModel.insertMovie(movie)
            }
            adapter.addMovie(movies)
            page++
        }
        isLoading = false
        progress_bar_loading.visibility = View.INVISIBLE
        // удаляем загрузчик
        loaderManager.destroyLoader(LOADER_ID)
    }

    // перезагрузка загрузчика
    override fun onLoaderReset(loader: Loader<JSONObject>) {
    }

    override fun onStart() {
        super.onStart()
        progress_bar_loading.visibility = View.INVISIBLE
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter()
        val layoutManager = GridLayoutManager(this@MainActivity, getColumnCount())

        main_recycler_view_poster.adapter = adapter
        main_recycler_view_poster.layoutManager = layoutManager
        switch_sort.isChecked = true
        // устанавливаем listener на switch
        switch_sort.setOnCheckedChangeListener { _, isChecked ->
            page = 1
            setMethodOfSort(isChecked)
        }

        switch_sort.isChecked = false

        // инициализируем интерфейс из MovieAdapter чтобы реализовать listener
        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                progress_bar_loading.visibility = View.VISIBLE
                val movie = adapter.movies[position]
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(INTENT_SAVE_MOVIE_ID, movie.id)

                startActivity(intent)
            }
        }

        // реализуем интерфейс для обновления данных для Recycler View
        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener {
            override fun onReachEnd() {
                if (!isLoading) {
                    setMethodOfSort(switch_sort.isChecked)
                }
            }

        }

        adapter.onLongClickListener = object : MovieAdapter.OnLongClickListener {
            override fun onLongClick(position: Int) {
                addFavouriteFromLongClick(position = position)
            }
        }

    }

    private fun addFavouriteFromLongClick(position: Int) {
        val movie = adapter.movies[position]
        val favouriteMovie = viewModel.getFavouriteMovieById(id = movie.id)
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(favouriteMovie = FavouriteMovie(movie = movie))
            super.showSnackBar(
                text = getString(R.string.add_fovourite_snackbar_text),
                coordinatorLayout = main_coordinator_layout
            )
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie = FavouriteMovie(movie = movie))
            super.showSnackBar(
                text = getString(R.string.remove_fovourite_snackbar_text),
                coordinatorLayout = main_coordinator_layout
            )

        }
    }

    private fun onClickTextView() {
        text_view_popularity.setOnClickListener {
            setMethodOfSort(false)
            switch_sort.isChecked = false
            text_view_popularity.setTextColor(resources.getColor(R.color.teal_200))
            text_view_top_rated.setTextColor(resources.getColor(R.color.white))
        }

        text_view_top_rated.setOnClickListener {
            setMethodOfSort(true)
            switch_sort.isChecked = true
            text_view_top_rated.setTextColor(resources.getColor(R.color.teal_200))
            text_view_popularity.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun setMethodOfSort(isTopRated: Boolean) {
        // применяет метод сортировки фильмов
        val methodOfSort = if (isTopRated) {
            NetworkUtils.TOP_RATED
        } else {
            NetworkUtils.POPULARITY
        }

        downloadData(methodOfSort = methodOfSort, page = page)
    }

    private fun downloadData(methodOfSort: Int, page: Int) {
        val url = NetworkUtils.buildURL(sortBy = methodOfSort, page = page, lang = lang)
        val bundle = Bundle()
        bundle.putString(SAVE_URL, url.toString())
        // пересоздание loader
        loaderManager.restartLoader(LOADER_ID, bundle, this)
    }

    private fun getColumnCount(): Int {
        // вычесляем кол-во колонок для GridLayout
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (width / 185 > 2) width / 185 else 2
    }


    companion object {
        const val INTENT_SAVE_MOVIE_ID = "INTENT_SAVE_MOVIE_ID"
        const val LOADER_ID = 132
        const val SAVE_URL = "SAVE_URL"
        private var page = 0
        private var isLoading = false
        private lateinit var lang: String
    }
}