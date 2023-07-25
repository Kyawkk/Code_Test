package com.kyawzinlinn.tmdb

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.databinding.ActivityMainBinding
import com.kyawzinlinn.tmdb.presentation.adapter.MovieItemAdapter
import com.kyawzinlinn.tmdb.presentation.viewmodel.MovieViewModel
import com.kyawzinlinn.tmdb.utils.Constants.IS_FAVORITE_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.Constants.MOVIE_ID_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.MovieType
import com.kyawzinlinn.tmdb.utils.Resource
import com.kyawzinlinn.tmdb.utils.dismissLoadingDialog
import com.kyawzinlinn.tmdb.utils.setUpLayoutTransition
import com.kyawzinlinn.tmdb.utils.showErrorSnackBar
import com.kyawzinlinn.tmdb.utils.showLoadingDialog
import com.kyawzinlinn.tmdb.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MovieViewModel
    private lateinit var loadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.parent.setUpLayoutTransition()

        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Loading...")
        loadingDialog.setCancelable(false)

        loadAllMovies()
    }

    private fun loadAllMovies() {
        loadPopularMovies()
        loadUpcomingMovies()
    }

    private fun loadUpcomingMovies() {
        viewModel.getUpcomingMovies("1")
        viewModel.upcomingMovies.observe(this){

            if (it.isLoading) loadingDialog.show()
            if (it.data != null) {
                loadingDialog.dismiss()
                setUpMoviesRecyclerview(MovieType.UPCOMING,it.data as List<Movie>)
            }
            if (it.error.isNotEmpty()){
                loadingDialog.dismiss()
                showErrorSnackBar(it.error){
                    loadUpcomingMovies()
                }
            }
        }
    }

    private fun setUpMoviesRecyclerview(movieType: MovieType, movies: List<Movie>) {
        when(movieType){
            MovieType.UPCOMING -> {
                binding.rvUpcomingMovies.setHasFixedSize(true)
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                binding.rvUpcomingMovies.layoutManager = layoutManager
                val adapter = MovieItemAdapter(movieType,movies, {movie ->
                    startMovieDetailActivity(movie)
                }, {id, isFavorite ->
                    viewModel.toggleFavorite(id, isFavorite)
                })
                binding.rvUpcomingMovies.adapter = adapter
            }
            MovieType.POPULAR -> {
                binding.rvPopularMovies.setHasFixedSize(true)
                binding.rvPopularMovies.layoutManager = LinearLayoutManager(this)
                val adapter = MovieItemAdapter(movieType,movies, {movie ->
                    startMovieDetailActivity(movie)
                },{id , isFavorite ->
                    viewModel.toggleFavorite(id,isFavorite)
                })
                binding.rvPopularMovies.adapter = adapter
            }
        }
    }

    private fun startMovieDetailActivity(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MOVIE_ID_INTENT_EXTRA,movie.id)
        intent.putExtra(IS_FAVORITE_INTENT_EXTRA,movie.isFavorite)
        startActivity(intent)
    }

    private fun loadPopularMovies() {
        viewModel.getPopularMovies("1")
        viewModel.popularMovies.observe(this){
            if (it.isLoading) loadingDialog.show()
            if (it.data != null) {
                loadingDialog.dismiss()
                setUpMoviesRecyclerview(MovieType.POPULAR,it.data as List<Movie>)
            }
            if (it.error.isNotEmpty()){
                loadingDialog.dismiss()
                showErrorSnackBar(it.error){
                    loadPopularMovies()
                }
            }
        }
    }
}