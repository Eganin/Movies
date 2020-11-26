package com.example.android.developers.mymovies.data.dataclasses

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
class FavouriteMovie(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var voteCount: Int,
    var title: String,
    var originalTitle: String,
    var overview: String,
    var posterPath: String,
    var bigPosterPath: String,
    var backdropPath: String,
    var voteAverage: Double,
    var releaseDate: String
) {
    @Ignore
    constructor(movie: Movie) : this(
        id = movie.id,
        voteCount = movie.voteCount,
        title = movie.title,
        originalTitle = movie.originalTitle,
        overview = movie.overview,
        posterPath = movie.posterPath,
        bigPosterPath = movie.bigPosterPath,
        backdropPath = movie.backdropPath,
        voteAverage = movie.voteAverage,
        releaseDate = movie.releaseDate
    )

    fun getMovie(): Movie {
        return Movie(
            id,
            voteCount,
            title,
            originalTitle,
            overview,
            posterPath,
            bigPosterPath,
            backdropPath,
            voteAverage,
            releaseDate
        )
    }
}