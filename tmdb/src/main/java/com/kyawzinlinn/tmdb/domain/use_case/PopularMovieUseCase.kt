package com.kyawzinlinn.tmdb.domain.use_case

import android.util.Log
import com.kyawzinlinn.tmdb.data.local.dao.FavoriteDao
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.local.database.toMovie
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.data.remote.dto.PopularMoviesDto
import com.kyawzinlinn.tmdb.data.remote.dto.toDatabaseMovie
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.utils.MovieType
import com.kyawzinlinn.tmdb.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PopularMovieUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao
) {
    operator fun invoke(page: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val type = MovieType.POPULAR
        val favoriteMovieIds = favoriteDao.getAllFavoriteIds().map { it.movieId }
        // Load cached movies with isFavorite info
        val cachedMoviesFlow = movieDao.getMovies(type.toString())
            .map { cachedMovies ->
                cachedMovies.toMovie().map { movie ->
                    movie.copy(isFavorite = movie.id in favoriteMovieIds)
                }
            }

        emit(Resource.Loading(data = cachedMoviesFlow.first()))

        try {
            val moviesFromApi = repository.getPopularMovies(page)

            moviesFromApi.results.map { movie -> movie.isFavorite = movie.id in favoriteMovieIds }

            emit(Resource.Success(data = moviesFromApi.results))
            withContext(Dispatchers.IO){
                movieDao.deleteMovies(type.toString())
                movieDao.insertAll(moviesFromApi.results.toDatabaseMovie(type.toString()))
            }
        }catch (e: Exception){
            when(e){
                is IOException -> emit(Resource.Error("Network Unavailable: Please check your internet connection and try again."))
                is HttpException -> emit(Resource.Error("An error occurred. Please check your internet connection."))
                else -> emit(Resource.Error(e.message.toString()))
            }
        }

        movieDao.getMovies(type.toString()).collect{
            emit(Resource.Success(it.toMovie()))
        }
    }
}