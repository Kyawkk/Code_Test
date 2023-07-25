package com.kyawzinlinn.tmdb.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.tmdb.data.local.dao.MovieDao
import com.kyawzinlinn.tmdb.data.local.database.toMovie
import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.data.remote.dto.PopularMoviesDto
import com.kyawzinlinn.tmdb.data.remote.dto.toDatabaseMovie
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.domain.use_case.MovieUseCase
import com.kyawzinlinn.tmdb.utils.MovieState
import com.kyawzinlinn.tmdb.utils.MovieType
import com.kyawzinlinn.tmdb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val repository: MovieRepository
): ViewModel() {

    private val _popularMovies = MutableLiveData<MovieState>()
    val popularMovies = _popularMovies

    private val _upcomingMovies = MutableLiveData<MovieState>()
    val upcomingMovies = _upcomingMovies

    private val _movieDetails = MutableLiveData<MovieState>()
    val movieDetails = _movieDetails

    private val _movieCasts = MutableLiveData<MovieState>()
    val movieCasts = _movieCasts

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    fun getPopularMovies(page: String){

        movieUseCase.popularMovieUseCase(page).onEach {
            when(it){
                is Resource.Loading -> _popularMovies.value = MovieState(isLoading = true)
                is Resource.Success -> _popularMovies.value = MovieState(data = it.data)
                is Resource.Error -> MovieState(error = it.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun getUpcomingMovies(page: String){
        movieUseCase.upComingMovieUseCase(page).onEach {
            when(it){
                is Resource.Loading -> _upcomingMovies.value = MovieState(isLoading = true)
                is Resource.Success -> _upcomingMovies.value = MovieState(data = it.data)
                is Resource.Error -> MovieState(error = it.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun getMovieDetails(movieId: String){
        movieUseCase.movieDetailsUseCase(movieId).onEach {
            when(it){
                is Resource.Loading -> _movieDetails.value = MovieState(isLoading = true)
                is Resource.Success -> _movieDetails.value = MovieState(data = it.data)
                is Resource.Error -> MovieState(error = it.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun getMovieCasts(movieId: String){
        movieUseCase.movieCastsUseCase(movieId).onEach {
            when(it){
                is Resource.Loading -> _movieCasts.value = MovieState(isLoading = true)
                is Resource.Success -> _movieCasts.value = MovieState(data = it.data)
                is Resource.Error -> MovieState(error = it.message.toString())
            }
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(movieId: String, isFavorite: Boolean){
        viewModelScope.launch {
            repository.toggleFavoriteMovie(movieId,isFavorite)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
