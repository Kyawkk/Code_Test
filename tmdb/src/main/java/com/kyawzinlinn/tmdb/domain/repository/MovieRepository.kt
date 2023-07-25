package com.kyawzinlinn.tmdb.domain.repository

import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.data.remote.dto.PopularMoviesDto
import io.reactivex.rxjava3.core.Observable

interface MovieRepository {
    suspend fun getPopularMovies(page: String): PopularMoviesDto
    suspend fun getUpcomingMovies(page: String): PopularMoviesDto
    suspend fun getMovieDetails(movieId: String): MovieDetailsDto
    suspend fun getCasts(movieId: String): CastsDto
    suspend fun toggleFavoriteMovie(movieId: String, isFavorite: Boolean)
}