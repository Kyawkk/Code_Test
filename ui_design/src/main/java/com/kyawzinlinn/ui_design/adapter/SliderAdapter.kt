package com.kyawzinlinn.ui_design.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.kyawzinlinn.ui_design.databinding.SliderItemBinding

class SliderAdapter: RecyclerView.Adapter<SliderAdapter.ViewHolder>() {
    class ViewHolder(private val binding: SliderItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SliderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 10

}