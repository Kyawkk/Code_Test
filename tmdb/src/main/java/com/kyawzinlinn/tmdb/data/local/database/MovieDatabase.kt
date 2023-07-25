package com.kyawzinlinn.tmdb.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kyawzinlinn.tmdb.data.local.dao.FavoriteDao
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao

@Database(entities = [DatabaseMovie::class,FavoriteMovieId::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}