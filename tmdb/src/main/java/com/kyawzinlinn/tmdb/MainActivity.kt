package com.kyawzinlinn.tmdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.databinding.ActivityMainBinding
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.presentation.adapter.MovieItemAdapter
import com.kyawzinlinn.tmdb.presentation.viewmodel.MovieViewModel
import com.kyawzinlinn.tmdb.utils.Constants.IS_FAVORITE_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.Constants.MOVIE_ID_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.MovieType
import com.kyawzinlinn.tmdb.utils.setUpLayoutTransition
import com.kyawzinlinn.tmdb.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MovieViewModel
    @Inject lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.parent.setUpLayoutTransition()

        loadAllMovies()
    }

    private fun loadAllMovies() {
        loadPopularMovies()
        loadUpcomingMovies()
    }

    private fun loadUpcomingMovies() {
        if(viewModel.upcomingMovies.value == null) viewModel.getUpcomingMovies("1")
        viewModel.upcomingMovies.observe(this){

            if (it.isLoading) binding.upcomingProgressBar.visibility = View.VISIBLE
            if (it.data != null) {
                binding.upcomingProgressBar.visibility = View.GONE
                setUpMovieRecyclerviews(MovieType.UPCOMING,it.data as List<Movie>)
            }
            if (it.error.isNotEmpty()){
                binding.upcomingProgressBar.visibility = View.GONE
                showErrorSnackBar(it.error){
                    loadUpcomingMovies()
                }
            }
        }
    }

    private fun setUpMovieRecyclerviews(movieType: MovieType, movies: List<Movie>) {
        when(movieType){
            MovieType.UPCOMING -> {
                setUpUpComingRecyclerview(movieType, movies)
            }
            MovieType.POPULAR -> {
                setUpPopularRecyclerview(movieType, movies)
            }
        }
    }

    private fun setUpPopularRecyclerview(
        movieType: MovieType,
        movies: List<Movie>
    ) {
        binding.rvPopularMovies.setHasFixedSize(true)
        binding.rvPopularMovies.layoutManager = LinearLayoutManager(this)
        val adapter = MovieItemAdapter(movieType, { movie ->
            startMovieDetailActivity(movie)
        }, { id, isFavorite ->
            viewModel.toggleFavorite(id, isFavorite)
        })
        binding.rvPopularMovies.adapter = adapter
        adapter.submitList(movies)
    }

    private fun setUpUpComingRecyclerview(
        movieType: MovieType,
        movies: List<Movie>
    ) {
        binding.rvUpcomingMovies.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvUpcomingMovies.layoutManager = layoutManager
        val adapter = MovieItemAdapter(movieType, { movie ->
            startMovieDetailActivity(movie)
        }, { id, isFavorite ->
            viewModel.toggleFavorite(id, isFavorite)
        })
        binding.rvUpcomingMovies.adapter = adapter
        adapter.submitList(movies)
    }

    private fun startMovieDetailActivity(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MOVIE_ID_INTENT_EXTRA,movie.id)
        intent.putExtra(IS_FAVORITE_INTENT_EXTRA,movie.isFavorite)
        startActivity(intent)
    }

    private fun loadPopularMovies() {
        // not to fetch data when orientation changes
        if(viewModel.popularMovies.value == null) viewModel.getPopularMovies("1")

        viewModel.popularMovies.observe(this){
            if (it.isLoading) binding.popularProgressBar.visibility = View.VISIBLE
            if (it.data != null) {
                binding.popularProgressBar.visibility = View.GONE
                setUpMovieRecyclerviews(MovieType.POPULAR,it.data as List<Movie>)
            }
            if (it.error.isNotEmpty()){
                binding.popularProgressBar.visibility = View.GONE
                showErrorSnackBar(it.error){
                    loadPopularMovies()
                }
            }
        }
    }
}