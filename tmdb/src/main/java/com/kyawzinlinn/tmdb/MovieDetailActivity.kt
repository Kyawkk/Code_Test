package com.kyawzinlinn.tmdb

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.databinding.ActivityMovieDetailBinding
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.presentation.adapter.CastItemAdapter
import com.kyawzinlinn.tmdb.presentation.viewmodel.MovieViewModel
import com.kyawzinlinn.tmdb.utils.Constants.IMG_URL_PREFIX_500
import com.kyawzinlinn.tmdb.utils.Constants.MOVIE_ID_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var binding: ActivityMovieDetailBinding
    @Inject lateinit var repository: MovieRepository
    private lateinit var dialog : ProgressDialog
    private var isFavorite = false
    private var movieId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        dialog = ProgressDialog(this)
        dialog.setMessage("Loading...")
        dialog.setCancelable(false)

        loadData()
        bindUI()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.apply {
            ivMovieDetailBack.setOnClickListener { onBackPressed() }
            ivFavorite.setOnClickListener { setFavorite() }
        }
    }

    private fun setFavorite() {
        isFavorite = !isFavorite
        if (isFavorite) binding.ivFavorite.load(R.drawable.round_favorite_24)
        else binding.ivFavorite.load(R.drawable.round_favorite_border_24)

        viewModel.toggleFavorite(movieId, isFavorite)
    }

    private fun loadData() {
        if (viewModel.movieDetails.value == null){
            movieId = viewModel.movieId.value ?: ""
            viewModel.getMovieDetailsAndCasts(movieId)
        }
    }

    private fun bindUI() {

        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = repository.isFavorite(movieId)
            withContext(Dispatchers.Main){
                if (isFavorite) binding.ivFavorite.load(R.drawable.round_favorite_24)
                else binding.ivFavorite.load(R.drawable.round_favorite_border_24)
            }
        }

        bindMovieDetailsUI()
        bindMovieCastUI()
    }

    private fun bindMovieCastUI() {
        viewModel.apply {
            movieCasts.observe(this@MovieDetailActivity){
                if (it.isLoading) binding.castProgressBar.visibility = View.VISIBLE
                if (!it.isLoading && it.data != null){
                    binding.castProgressBar.visibility = View.GONE
                    val casts = it.data as CastsDto
                    setUpCastRecyclerView(casts)
                }
                if (it.error.isNotEmpty()){
                    binding.castProgressBar.visibility = View.GONE
                    showErrorSnackBar(it.error){
                        loadData()
                    }
                }
            }
        }
    }

    private fun bindMovieDetailsUI() {
        viewModel.apply {
            movieDetails.observe(this@MovieDetailActivity){

                if (it.isLoading){
                    dialog.show()
                }
                if (!it.isLoading && it.data != null){
                    dialog.dismiss()
                    val movieDetails = it.data as MovieDetailsDto
                    binding.apply {
                        ivMovieDetailPoster.load("${IMG_URL_PREFIX_500}${movieDetails.poster_path}")
                        tvMovieDetailName.text = movieDetails.title
                        tvMovieDetailDescription.text = movieDetails.overview
                        tvMovieDetailYear.text = movieDetails.release_date
                    }
                }

                if (it.error.isNotEmpty()){
                    dialog.dismiss()
                    showErrorSnackBar(it.error){
                        loadData()
                    }
                }
            }
        }
    }

    private fun setUpCastRecyclerView(casts: CastsDto) {
        binding.apply {
            rvMovieCast.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this@MovieDetailActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvMovieCast.layoutManager = layoutManager
            val adapter = CastItemAdapter()
            rvMovieCast.adapter = adapter
            adapter.submitList(casts.cast)
        }
    }
}