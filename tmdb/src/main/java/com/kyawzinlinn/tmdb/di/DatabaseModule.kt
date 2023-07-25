package com.kyawzinlinn.tmdb.di

import android.content.Context
import androidx.room.Room
import com.kyawzinlinn.tmdb.data.local.dao.FavoriteDao
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.local.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext application: Context): MovieDatabase {
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            "Movie"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun providesFavoriteDao(database: MovieDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}