package com.example.android.developers.mymovies.data.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
class Movie(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val voteCount: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val bigPosterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val releaseDate: String
) {
    fun getFavouriteMovie(): FavouriteMovie {
        return FavouriteMovie(
            movie = this
        )
    }
}