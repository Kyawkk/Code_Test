package com.kyawzinlinn.ui_design.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kyawzinlinn.ui_design.databinding.RateItemBinding
import com.kyawzinlinn.ui_design.databinding.RoomItemBinding
import com.kyawzinlinn.ui_design.utils.OptionType

class ItemAdapter (private val optionType: OptionType): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class RoomViewHolder(private val binding: RoomItemBinding): RecyclerView.ViewHolder(binding.root){}

    class RateViewHolder(private val binding: RateItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(optionType){
            OptionType.ROOM -> RoomViewHolder(RoomItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            OptionType.RATE -> RateViewHolder(RateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount() = 3

}