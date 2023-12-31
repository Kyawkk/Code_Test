package com.kyawzinlinn.tmdb.data.remote.dto

import com.kyawzinlinn.tmdb.data.local.database.DatabaseMovie

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var isFavorite: Boolean
)

fun List<Movie>.toDatabaseMovie(type: String): List<DatabaseMovie>{

    return map{
        DatabaseMovie(
            0,
            it.id,
            it.adult,
            it.backdrop_path,
            it.original_language,
            it.original_title,
            it.overview,
            it.popularity,
            it.poster_path,
            it.release_date,
            it.title,
            it.video,
            it.vote_average,
            it.vote_count,
            type,
            it.isFavorite
        )
    }
}