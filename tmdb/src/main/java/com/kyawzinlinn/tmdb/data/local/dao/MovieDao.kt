package com.kyawzinlinn.tmdb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.tmdb.data.local.database.DatabaseMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie where type = :type")
    fun getMovies(type: String): Flow<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<DatabaseMovie>)

    @Query("delete from Movie where type = :type")
    suspend fun deleteMovie(type: String)

    @Query("UPDATE Movie SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun setMovieFavorite(movieId: String, isFavorite: Boolean)
}