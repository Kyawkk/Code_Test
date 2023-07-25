package com.kyawzinlinn.tmdb

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.kyawzinlinn.tmdb.data.remote.dto.CastsDto
import com.kyawzinlinn.tmdb.data.remote.dto.MovieDetailsDto
import com.kyawzinlinn.tmdb.databinding.ActivityMovieDetailBinding
import com.kyawzinlinn.tmdb.databinding.CastItemBinding
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.presentation.adapter.CastItemAdapter
import com.kyawzinlinn.tmdb.presentation.viewmodel.MovieViewModel
import com.kyawzinlinn.tmdb.utils.Constants.IMG_URL_PREFIX_500
import com.kyawzinlinn.tmdb.utils.Constants.IS_FAVORITE_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.Constants.MOVIE_ID_INTENT_EXTRA
import com.kyawzinlinn.tmdb.utils.showErrorSnackBar
import com.kyawzinlinn.tmdb.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    @Inject lateinit var viewModel: MovieViewModel
    private lateinit var binding: ActivityMovieDetailBinding
    @Inject lateinit var repository: MovieRepository
    private lateinit var dialog : ProgressDialog
    private var isFavorite = false
    private var movieId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog.setMessage("Loading...")
        dialog.setCancelable(false)

        isFavorite = intent?.extras?.getBoolean(IS_FAVORITE_INTENT_EXTRA) as Boolean

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
        movieId = intent?.extras?.getString(MOVIE_ID_INTENT_EXTRA).toString()
        viewModel.getMovieDetails(movieId)
        viewModel.getMovieCasts(movieId)
    }

    private fun bindUI() {

        if (isFavorite) binding.ivFavorite.load(R.drawable.round_favorite_24)
        else binding.ivFavorite.load(R.drawable.round_favorite_border_24)

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

            movieCasts.observe(this@MovieDetailActivity){
                Log.d("TAG", "bindUI: $it")
                if (it.isLoading) {dialog.show()}
                if (!it.isLoading && it.data != null){
                    dialog.dismiss()
                    val casts = it.data as CastsDto
                    setUpCastRecyclerView(casts)
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