package com.kyawzinlinn.tmdb.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kyawzinlinn.tmdb.data.remote.dto.Movie

@Entity("Movie")
data class DatabaseMovie(
    @PrimaryKey(autoGenerate = true)
    val index: Int,
    @ColumnInfo("id") val movieId: String,
    @ColumnInfo("adult") val adult: Boolean,
    @ColumnInfo("backdrop_path") val backdrop_path: String?,
    @ColumnInfo("original_language") val original_language: String,
    @ColumnInfo("original_title") val original_title: String,
    @ColumnInfo("overview") val overview: String,
    @ColumnInfo("popularity") val popularity: Double,
    @ColumnInfo("poster_path") val poster_path: String?,
    @ColumnInfo("release_date") val release_date: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("video") val video: Boolean,
    @ColumnInfo("vote_average") val vote_average: Double,
    @ColumnInfo("vote_count") val vote_count: Int,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("isFavorite") var isFavorite: Boolean
)
fun List<DatabaseMovie>.toMovie(): List<Movie>{
    return map {
        Movie(
            it.adult,
            it.backdrop_path!!,
            listOf(),
            it.movieId,
            it.original_language,
            it.original_title,
            it.overview,
            it.popularity,
            it.poster_path!!,
            it.release_date,
            it.title,
            it.video,
            it.vote_average,
            it.vote_count,
            it.isFavorite
        )
    }
}