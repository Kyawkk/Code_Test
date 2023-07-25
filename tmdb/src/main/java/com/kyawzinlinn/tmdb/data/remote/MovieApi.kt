package com.kyawzinlinn.tmdb.data.remote

import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.data.remote.dto.PopularMoviesDto
import com.kyawzinlinn.tmdb.utils.Constants.TOKEN
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Query("page")page: String
    ): PopularMoviesDto

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Query("page")page: String
    ): PopularMoviesDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Path("movie_id") movieId: String
    ): MovieDetailsDto

    @GET("movie/{movie_id}/credits")
    suspend fun getCasts(
        @Header("Authorization") token: String = "Bearer ".plus(TOKEN),
        @Path("movie_id") movieId: String
    ): CastsDto
}