package com.kyawzinlinn.tmdb.domain.use_case

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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpComingMovieUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao
) {
    operator fun invoke(page: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val type = MovieType.UPCOMING
        val cachedMovies = movieDao.getMovies(type.toString()).toMovie()
        val favoriteMovieIds = favoriteDao.getAllFavoriteIds().map { it.movieId }

        cachedMovies.forEach { movie ->
            movie.isFavorite = movie.id in favoriteMovieIds
        }
        emit(Resource.Loading(data = cachedMovies))

        try {
            val moviesFromApi = repository.getUpcomingMovies(page).results
            moviesFromApi.forEach { movie -> movie.isFavorite = movie.id in favoriteMovieIds }
            emit(Resource.Success(data = moviesFromApi))

            withContext(Dispatchers.IO){
                movieDao.deleteMovies(type.toString())
                movieDao.insertAll(moviesFromApi.toDatabaseMovie(type.toString()))
            }
        }catch (e: Exception){
            when(e){
                is IOException -> emit(Resource.Error("Network Unavailable: Please check your internet connection and try again."))
                is HttpException -> emit(Resource.Error("An error occurred. Please check your internet connection."))
                else -> emit(Resource.Error(e.message.toString()))
            }
        }

        val newMovies = movieDao.getMovies(type.toString()).toMovie()
        emit(Resource.Success(newMovies))
    }
}