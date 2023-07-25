package com.kyawzinlinn.tmdb.domain.use_case

import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieCastsUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val movieDao: MovieDao
) {
    operator fun invoke(page: String): Flow<Resource<CastsDto>> = flow {
        emit(Resource.Loading())

        try {
            val movieCasts = withContext(Dispatchers.IO){
                repository.getCasts(page)
            }
            emit(Resource.Success(data = movieCasts))
        }catch (e: Exception){
            when(e){
                is IOException -> emit(Resource.Error("Network Unavailable: Please check your internet connection and try again."))
                is HttpException -> emit(Resource.Error("An error occurred. Please check your internet connection."))
                else -> emit(Resource.Error(e.message.toString()))
            }
        }
    }
}