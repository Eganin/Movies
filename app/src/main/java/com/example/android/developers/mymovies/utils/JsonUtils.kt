package com.example.android.developers.mymovies.utils

import android.util.DisplayMetrics
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.dataclasses.Review
import com.example.android.developers.mymovies.data.dataclasses.Trailer
import org.json.JSONObject


class JsonUtils {
    companion object {
        // для информации о фильме
        private const val KEY_RESULTS = "results"
        private const val KEY_ID = "id"
        private const val KEY_VOTE_COUNT = "vote_count"
        private const val KEY_TITLE = "title"
        private const val KEY_ORIGINAL_TITLE = "original_title"
        private const val KEY_OVERVIEW = "overview"
        private const val KEY_POSTER_PATH = "poster_path"
        private const val KEY_BACK_DROP_PATH = "backdrop_path"
        private const val KEY_VOTE_AVERAGE = "vote_average"
        private const val KEY_RELEASE_DATE = "release_date"

        private const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        private const val SMALL_POSTER_SIZE = "w185"
        private const val BIG_POSTER_SIZE = "w780"

        //  для информации о рецензиях
        private const val KEY_AUTHOR = "author"
        private const val KEY_CONTENT = "content"

        // для информации о видео
        private const val KEY_VIDEO = "key"
        private const val KEY_VIDEO_NAME = "name"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v=%s"


        fun getMoviesFromJSON(jsonObject: JSONObject?): List<Movie> {
            val result = arrayListOf<Movie>()

            val jsonArray = jsonObject?.getJSONArray(KEY_RESULTS) ?: return result
            for (i in 0 until jsonArray.length()) {
                val objectMovie = jsonArray.getJSONObject(i)
                val id = objectMovie.getInt(KEY_ID)
                val voteCount = objectMovie.getInt(KEY_VOTE_COUNT)
                val title = objectMovie.getString(KEY_TITLE)
                val originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE)
                val overview = objectMovie.getString(KEY_OVERVIEW)
                val posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE +
                        objectMovie.getString(KEY_POSTER_PATH)
                val bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE +
                        objectMovie.getString(KEY_POSTER_PATH)
                val backdropPath = objectMovie.getString(KEY_BACK_DROP_PATH)
                val voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE)
                val releaseDate = objectMovie.getString(KEY_RELEASE_DATE)

                result.add(
                    Movie(
                        id = id,
                        voteCount = voteCount,
                        title = title,
                        originalTitle = originalTitle,
                        overview = overview,
                        posterPath = posterPath,
                        bigPosterPath = bigPosterPath,
                        backdropPath = backdropPath,
                        voteAverage = voteAverage,
                        releaseDate = releaseDate
                    )
                )
            }

            return result
        }

        fun getReviewsFromJson(jsonObject: JSONObject?): List<Review> {
            val resultReviews = arrayListOf<Review>()

            val jsonArray = jsonObject?.getJSONArray(KEY_RESULTS) ?: return resultReviews
            println(jsonArray.toString())
            for (i in 0 until jsonArray.length()) {
                try {
                    val objectReview = jsonArray.getJSONObject(i)
                    val author = objectReview.getString(KEY_AUTHOR)
                    val content = objectReview.getString(KEY_CONTENT)
                    resultReviews.add(Review(author = author, content = content))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            return resultReviews
        }

        fun getVideosFromJson(jsonObject: JSONObject?): List<Trailer> {
            val resultTrailers = arrayListOf<Trailer>()

            val jsonArray = jsonObject?.getJSONArray(KEY_RESULTS) ?: return resultTrailers
            for (i in 0 until jsonArray.length()) {
                try {
                    val objectVideo = jsonArray.getJSONObject(i)
                    val key = BASE_URL_YOUTUBE.format(objectVideo.getString(KEY_VIDEO))
                    val name = objectVideo.getString(KEY_VIDEO_NAME)
                    resultTrailers.add(Trailer(key = key, name = name))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            return resultTrailers
        }
    }

}