package com.kyawzinlinn.tmdb.di

import com.kyawzinlinn.tmdb.data.local.dao.FavoriteDao
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.remote.MovieApi
import com.kyawzinlinn.tmdb.data.repository_impl.MovieRepositoryImpl
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.domain.use_case.MovieUseCase
import com.kyawzinlinn.tmdb.presentation.viewmodel.MovieViewModel
import com.kyawzinlinn.tmdb.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPostRepository(api: MovieApi, movieDao: MovieDao, favoriteDao: FavoriteDao): MovieRepository {
        return MovieRepositoryImpl(api, movieDao,favoriteDao)
    }

}