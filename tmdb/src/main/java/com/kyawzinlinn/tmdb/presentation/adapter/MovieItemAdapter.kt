package com.kyawzinlinn.tmdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.tmdb.R
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.databinding.PopularMovieItemBinding
import com.kyawzinlinn.tmdb.databinding.UpcomingMovieItemBinding
import com.kyawzinlinn.tmdb.domain.repository.MovieRepository
import com.kyawzinlinn.tmdb.utils.Constants.IMG_URL_PREFIX_300
import com.kyawzinlinn.tmdb.utils.MovieType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieItemAdapter( private val movieType: MovieType, private val onMovieClick: (Movie) -> Unit ,private val onFavoriteClick: (String,Boolean) -> Unit): ListAdapter<Movie, RecyclerView.ViewHolder>(DiffCallBack) {

    class PopularMovieViewHolder(private val binding: PopularMovieItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(movie: Movie, onFavoriteClick: (String,Boolean) -> Unit){

            var isFavorite = movie.isFavorite

            if (isFavorite) binding.ivPopularFavorite.load(R.drawable.round_favorite_24)
            else binding.ivPopularFavorite.load(R.drawable.round_favorite_border_24)

            binding.apply {
                ivMoviePoster.load("${IMG_URL_PREFIX_300}${movie.poster_path}")
                tvPopularMovieTitle.text = movie.title
                tvPopularMovieDesc.text = movie.overview

                if (isFavorite) ivPopularFavorite.load(R.drawable.round_favorite_24)
                else ivPopularFavorite.load(R.drawable.round_favorite_border_24)

                ivPopularFavorite.setOnClickListener {
                    isFavorite = !isFavorite

                    if (isFavorite) ivPopularFavorite.load(R.drawable.round_favorite_24)
                    else ivPopularFavorite.load(R.drawable.round_favorite_border_24)

                    onFavoriteClick(movie.id,isFavorite)
                }
            }
        }
    }

    class UpComingMovieViewHolder(private val binding: UpcomingMovieItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie, onFavoriteClick: (String,Boolean) -> Unit){
            var isFavorite = movie.isFavorite

            // always update isFavorite Status
            if (isFavorite) binding.ivUpcomingFavorite.load(R.drawable.round_favorite_24)
            else binding.ivUpcomingFavorite.load(R.drawable.round_favorite_border_24)

            binding.apply {
                ivMoviePoster.load("${IMG_URL_PREFIX_300}${movie.poster_path}")
                tvMovieTitle.text = movie.title
                tvVotePercent.text = "${(movie.vote_average * 10)} %"

                if (isFavorite) ivUpcomingFavorite.load(R.drawable.round_favorite_24)
                else ivUpcomingFavorite.load(R.drawable.round_favorite_border_24)

                ivUpcomingFavorite.setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) ivUpcomingFavorite.load(R.drawable.round_favorite_24)
                    else ivUpcomingFavorite.load(R.drawable.round_favorite_border_24)

                    onFavoriteClick(movie.id,isFavorite)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(movieType){
            MovieType.POPULAR -> PopularMovieViewHolder(PopularMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false))

            MovieType.UPCOMING -> UpComingMovieViewHolder(UpcomingMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onMovieClick(getItem(position)) }
        when(holder){
            is PopularMovieViewHolder -> holder.bind(getItem(position), onFavoriteClick)
            is UpComingMovieViewHolder -> holder.bind(getItem(position), onFavoriteClick)
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

    }
}