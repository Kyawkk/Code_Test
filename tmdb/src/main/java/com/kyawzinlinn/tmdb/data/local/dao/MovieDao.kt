package com.kyawzinlinn.tmdb.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.tmdb.data.local.database.DatabaseMovie
import com.kyawzinlinn.tmdb.data.remote.dto.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie where type = :type")
    suspend fun getMovies(type: String): List<DatabaseMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<DatabaseMovie>)

    @Query("delete from Movie where type = :type")
    suspend fun deleteMovies(type: String)
}