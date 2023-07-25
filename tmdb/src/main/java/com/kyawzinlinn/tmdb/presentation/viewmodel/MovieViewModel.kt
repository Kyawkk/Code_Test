package com.kyawzinlinn.tmdb.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.domain.use_case.MovieUseCase
import com.kyawzinlinn.tmdb.utils.MovieState
import com.kyawzinlinn.tmdb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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

    private val _movieId = savedStateHandle.getLiveData<String>("movie_id")
    val movieId: LiveData<String> get() = _movieId

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

    fun getMovieDetailsAndCasts(movieId: String){
        movieUseCase.movieDetailsUseCase(movieId).onEach {
            when(it){
                is Resource.Loading -> _movieDetails.value = MovieState(isLoading = true)
                is Resource.Success -> _movieDetails.value = MovieState(data = it.data)
                is Resource.Error -> MovieState(error = it.message.toString())
            }
        }.launchIn(viewModelScope)

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
