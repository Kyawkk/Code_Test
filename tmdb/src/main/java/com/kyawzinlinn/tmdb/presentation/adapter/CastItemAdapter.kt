package com.kyawzinlinn.tmdb.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kyawzinlinn.tmdb.data.remote.dto.Cast
import com.kyawzinlinn.tmdb.databinding.CastItemBinding
import com.kyawzinlinn.tmdb.utils.Constants.IMG_URL_PREFIX_300

class CastItemAdapter : ListAdapter<Cast, CastItemAdapter.ViewHolder>(DiffCallBack) {
    class ViewHolder (private val binding: CastItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cast: Cast){
            binding.apply {
                ivCastProfile.load("${IMG_URL_PREFIX_300}${cast.profile_path}")
                tvCastName.text = cast.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Cast>(){
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

    }
}