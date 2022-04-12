package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.LayoutSongBinding
import com.example.musicplayer.model.SongResponse

class SonglistAdapter(var songlist: ArrayList<SongResponse>,var itemclick:AdapterClickListerner): RecyclerView.Adapter<SonglistAdapter.MyViewHolder>(){

    class MyViewHolder(var binding: LayoutSongBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutSongBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item=songlist[position]

        holder.binding.song.text=item.songname

        holder.binding.song.setOnClickListener {
            itemclick.onItemClick(it,position,songlist[position])

        }
    }

    override fun getItemCount(): Int {
        return songlist.size
    }

}