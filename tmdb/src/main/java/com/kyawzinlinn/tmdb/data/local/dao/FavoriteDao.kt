package com.kyawzinlinn.tmdb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.tmdb.data.local.database.FavoriteMovieId
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteMovie(movieId: FavoriteMovieId)

    @Query("select * from favorite")
    suspend fun getAllFavoriteIds(): List<FavoriteMovieId>

    @Query("delete from favorite where movie_id = :movieId")
    suspend fun deleteFavoriteId(movieId: String)
}