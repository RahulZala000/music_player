package com.example.musicplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.LayoutSongBinding
import com.example.musicplayer.model.Item
import com.example.musicplayer.ui.fragment.HomeFragment

class SeachSongAdapater(var click: HomeFragment):RecyclerView.Adapter<SeachSongAdapater.MyViewHolder>(){

    var song=ArrayList<Item>()

    class MyViewHolder(var binding:LayoutSongBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(LayoutSongBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item=song[position]
        holder.binding.song.text=item.data.name

        holder.binding.song.setOnClickListener{
         click.songd(item)
        }
    }

    override fun getItemCount(): Int {
        return song.size
    }

    fun Seachsong(s: List<Item>)
    {
        song.addAll(s)
        notifyDataSetChanged()
    }
    interface songdata{
        fun songd(d:Item)
    }

}