package com.example.android.developers.mymovies.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.loader.content.AsyncTaskLoader
import com.example.android.developers.mymovies.activites.MainActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import kotlin.Exception
import kotlin.text.StringBuilder

class NetworkUtils {
    companion object {
        private const val API_KEY = "0d2c6c8ec17d73125f9c754d030f02eb"
        private const val BASE_URL = "https://api.themoviedb.org/3/discover/movie"
        private const val BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos"
        private const val BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews"
        private const val PARAMS_API_KEY = "api_key"
        private const val PARAMS_LANGUAGE = "language"
        private const val PARAMS_SORT_BY = "sort_by"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"
        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val SORT_BY_TOP_RATED = "vote_average.desc"
        private const val MIN_VOTE_COUNT_VALUE = "1000"

        const val POPULARITY = 0
        const val TOP_RATED = 1

        fun buildURL(sortBy: Int, page: Int , lang : String): URL {
            // составление запроса - URL
            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(
                    PARAMS_SORT_BY,
                    if (sortBy == POPULARITY) SORT_BY_POPULARITY else SORT_BY_TOP_RATED
                ).appendQueryParameter(PARAMS_PAGE, page.toString())
                .build()
            return URL(uri.toString())
        }

        fun buildUrlToVideo(id: Int, lang : String): URL {
            val uri = Uri.parse(BASE_URL_VIDEOS.format(id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .build()

            return URL(uri.toString())
        }

        fun buildUrlToReviews(id: Int, lang : String): URL {
            val uri = Uri.parse(BASE_URL_REVIEWS.format(id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE , lang)
                .build()

            return URL(uri.toString())
        }


        fun getJsonFromNetwork(sortBy: Int, page: Int, lang : String): JSONObject? {
            val url = buildURL(sortBy = sortBy, page = page,lang=lang)
            val json = NetworkUtils().JSONLoadTask().execute(url)?.get() ?: return null
            return json
        }

        fun getJsonFromNetworkToVideos(id: Int, lang : String): JSONObject? {
            val url = buildUrlToVideo(id = id,lang=lang)
            val json = NetworkUtils().JSONLoadTask().execute(url)?.get() ?: return null
            return json
        }

        fun getJsonFromNetworkToReviews(id: Int, lang : String): JSONObject? {
            val url = buildUrlToReviews(id = id,lang=lang)
            val json = NetworkUtils().JSONLoadTask().execute(url)?.get() ?: return null
            return json
        }


    }

    inner class JSONLoadTask : AsyncTask<URL, Void, JSONObject>() {
        override fun doInBackground(vararg params: URL?): JSONObject? {
            if (params.isEmpty()) {
                return null
            }
            try {
                val stringBuilder = StringBuilder()
                val connection = params[0]?.openConnection() ?: return null
                val inputStream = connection.getInputStream()
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var line = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = bufferedReader.readLine()
                }

                return JSONObject(stringBuilder.toString())
            } catch (e: Exception) {
                return null
            }

        }

    }

    //работает при изменении ориентации - загружает json
    class JsonLoader(context: Context, val bundle: Bundle?) :
        AsyncTaskLoader<JSONObject>(context) {

        interface OnStartLoadingListener {
            fun onStartLoading()
        }

        var onStartLoadingListener: OnStartLoadingListener? = null

        override fun loadInBackground(): JSONObject? {
            val urlAsString = bundle?.getString(MainActivity.SAVE_URL) ?: return null

            val url = URL(urlAsString)
            try {
                val stringBuilder = StringBuilder()
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var line = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = bufferedReader.readLine()
                }

                return JSONObject(stringBuilder.toString())
            } catch (e: Exception) {
                return null
            }
        }

        override fun onStartLoading() {
            super.onStartLoading()
            // продолжить загрузку
            onStartLoadingListener?.onStartLoading()
            forceLoad()
        }

    }


}