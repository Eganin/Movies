package com.example.android.developers.mymovies.data.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie
import com.example.android.developers.mymovies.data.dataclasses.Movie

@Dao
interface MovieDao {
    @Query(value = "SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query(value = "SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int) : Movie

    @Query(value = "DELETE FROM movies")
    fun deleteAllMovies()

    @Delete
    fun deleteMovie(movie: Movie)

    @Insert
    fun insertMovie(movie: Movie)


    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>



    @Query(value = "SELECT * FROM favourite_movies WHERE id == :favouriteMovieId")
    fun getFavouriteMovieById(favouriteMovieId: Int) : FavouriteMovie

    @Query(value = "DELETE FROM favourite_movies")
    fun deleteAllFavouriteMovies()

    @Delete
    fun deleteFavouriteMovie(favouriteMovie: FavouriteMovie)

    @Insert
    fun insertFavourite(favouriteMovie: FavouriteMovie)

    @Query("SELECT * FROM favourite_movies")
    fun test() : List<Movie>
}