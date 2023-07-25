package com.kyawzinlinn.tmdb.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.tmdb.R
import com.kyawzinlinn.tmdb.data.remote.dto.Movie
import com.kyawzinlinn.tmdb.databinding.PopularMovieItemBinding
import com.kyawzinlinn.tmdb.databinding.UpcomingMovieItemBinding
import com.kyawzinlinn.tmdb.utils.Constants.IMG_URL_PREFIX_300
import com.kyawzinlinn.tmdb.utils.MovieType

class MovieItemAdapter(private val movieType: MovieType, private val movies: List<Movie>, private val onMovieClick: (Movie) -> Unit ,private val onFavoriteClick: (String,Boolean) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PopularMovieViewHolder(private val binding: PopularMovieItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie, onFavoriteClick: (String,Boolean) -> Unit){
            var isFavorite = movie.isFavorite!!
            binding.apply {
                ivMoviePoster.load("${IMG_URL_PREFIX_300}${movie.poster_path}")
                tvPopularMovieTitle.text = movie.title
                tvPopularMovieDesc.text = movie.overview

                Log.d("TAG", "isFavorite: ${movie.isFavorite} ${movie.title}")
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
            var isFavorite = movie.isFavorite!!

            binding.apply {
                ivMoviePoster.load("${IMG_URL_PREFIX_300}${movie.poster_path}")
                tvMovieTitle.text = movie.title
                tvVotePercent.text = "${(movie.vote_average * 10)} %"

                Log.d("TAG", "isFavorite: upcoming $isFavorite")

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
        holder.itemView.setOnClickListener { onMovieClick(movies.get(position)) }
        when(holder){
            is PopularMovieViewHolder -> holder.bind(movies.get(position), onFavoriteClick)
            is UpComingMovieViewHolder -> holder.bind(movies.get(position), onFavoriteClick)
        }
    }

    override fun getItemCount() = movies.size
}