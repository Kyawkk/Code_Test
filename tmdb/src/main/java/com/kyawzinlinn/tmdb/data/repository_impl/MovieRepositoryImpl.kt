package com.kyawzinlinn.tmdb.data.repository_impl

import com.kyawzinlinn.tmdb.data.local.dao.FavoriteDao
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.local.database.FavoriteMovieId
import com.kyawzinlinn.tmdb.data.local.database.toMovie
import com.kyawzinlinn.tmdb.data.remote.MovieApi
import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.data.remote.dto.PopularMoviesDto
import com.kyawzinlinn.tmdb.data.remote.dto.toDatabaseMovie
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.utils.MovieType
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val favoriteDao: FavoriteDao
) : MovieRepository{
    override suspend fun getPopularMovies(page: String): PopularMoviesDto {
        return api.getPopularMovies(page = page)
    }

    override suspend fun getUpcomingMovies(page: String): PopularMoviesDto {
        return api.getUpcoming(page = page)
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetailsDto {
        return api.getMovieDetails(movieId = movieId)
    }

    override suspend fun getCasts(movieId: String): CastsDto {
        return api.getCasts(movieId = movieId)
    }

    override suspend fun toggleFavoriteMovie(movieId: String, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isFavorite) favoriteDao.addFavoriteMovie(FavoriteMovieId(movieId = movieId))
            else favoriteDao.deleteFavoriteId(movieId)
        }
    }

}