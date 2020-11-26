package com.example.android.developers.mymovies.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.developers.mymovies.data.dataclasses.FavouriteMovie
import com.example.android.developers.mymovies.data.dataclasses.Movie
import com.example.android.developers.mymovies.data.interfaces.MovieDao

@Database(entities = [Movie::class, FavouriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDao
    companion object {
        private var database: MovieDatabase? = null
        private const val DB_NAME = "movies.db"

        fun getInstance(context: Context): MovieDatabase? {
            if (database == null) {
                synchronized(MovieDatabase::class) {
                    database = Room.databaseBuilder(context, MovieDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigrationFrom()//миграция при изменении БД
                        .build()
                }
            }
            return database
        }

        fun destroyDB() {
            database = null
        }
    }
}