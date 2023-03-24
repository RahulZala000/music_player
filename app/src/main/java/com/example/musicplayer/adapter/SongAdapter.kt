package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.LayoutSongBinding
import com.example.musicplayer.model.SongResponse


class SongAdapter(var songlist: ArrayList<SongResponse>, var click: AdapterClickListerner):RecyclerView.Adapter<SongAdapter.MyViewHolder>(){

    class MyViewHolder(var binding:LayoutSongBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(LayoutSongBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SongAdapter.MyViewHolder, position: Int) {
        var item=songlist[position]

        holder.binding.song.text=item.songName

        holder.binding.song.setOnClickListener {
            click.onItemClick(it,position)
        }
    }

    override fun getItemCount(): Int {
       return songlist.size
    }
}