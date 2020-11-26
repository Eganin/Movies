package com.example.android.developers.mymovies.data.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.database.MovieDatabase
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = MovieDatabase.getInstance(application.applicationContext)

    var movies = database?.movieDao()?.getAllMovies()
    var favouriteMovies = database?.movieDao()?.getAllFavouriteMovies()

    fun getMovieById(id: Int): Movie {
        return GetMovieByIdTask().execute(id).get()
    }

    fun insertMovie(movie: Movie) {
        InsertMovieTask().execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieTask().execute(movie)
    }

    fun deleteAllMovies() {
        DeleteAllMoviesTask().execute()
    }

    fun getFavouriteMovieById(id: Int): FavouriteMovie? {
        return GetFavouriteMovieByIdTask().execute(id).get()
    }

    fun insertFavouriteMovie(favouriteMovie: FavouriteMovie) {
        InsertFavouriteMovieTask().execute(favouriteMovie)
    }

    fun deleteFavouriteMovie(favouriteMovie: FavouriteMovie) {
        DeleteFavouriteMovieTask().execute(favouriteMovie)
    }

    fun deleteAllFavouriteMovies() {
        DeleteAllFavouriteMoviesTask().execute()
    }


    inner class InsertMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg params: Movie?): Void? {
            params[0]?.let { database?.movieDao()?.insertMovie(movie = it) }
            return null
        }
    }

    inner class DeleteMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg params: Movie?): Void? {
            params[0]?.let { database?.movieDao()?.deleteMovie(movie = it) }
            return null
        }
    }

    inner class DeleteAllMoviesTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            database?.movieDao()?.deleteAllMovies()
            return null
        }

    }

    inner class GetMovieByIdTask : AsyncTask<Int, Void, Movie>() {
        override fun doInBackground(vararg params: Int?): Movie? {
            return params[0]?.let { database?.movieDao()?.getMovieById(movieId = it) }
        }
    }

    inner class InsertFavouriteMovieTask : AsyncTask<FavouriteMovie, Void, Void>() {
        override fun doInBackground(vararg params: FavouriteMovie?): Void? {
            params[0]?.let { database?.movieDao()?.insertFavourite(favouriteMovie = it) }
            return null
        }
    }

    inner class DeleteFavouriteMovieTask : AsyncTask<FavouriteMovie, Void, Void>() {
        override fun doInBackground(vararg params: FavouriteMovie?): Void? {
            params[0]?.let { database?.movieDao()?.deleteFavouriteMovie(favouriteMovie = it) }
            return null
        }
    }

    inner class DeleteAllFavouriteMoviesTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            database?.movieDao()?.deleteAllFavouriteMovies()
            return null
        }

    }

    inner class GetFavouriteMovieByIdTask : AsyncTask<Int, Void, FavouriteMovie>() {
        override fun doInBackground(vararg params: Int?): FavouriteMovie? {
            return params[0]?.let {
                database?.movieDao()?.getFavouriteMovieById(favouriteMovieId = it)
            }
        }
    }





}