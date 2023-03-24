package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.LayoutSongBinding
import com.example.musicplayer.model.SongResponse

class SongListAdapter(var songList: ArrayList<SongResponse>, var click:((pos:Int)->Unit), var longClick:((pos:Int)->Unit)): RecyclerView.Adapter<SongListAdapter.MyViewHolder>(){

    class MyViewHolder(var binding: LayoutSongBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutSongBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item=songList[position]

        holder.binding.song.text=item.songName

        holder.binding.song.setOnClickListener {
            click(position)
        }

        holder.binding.song.setOnLongClickListener {
            longClick(position)
             true
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    fun searchList(SongList: ArrayList<SongResponse>)
    {

        songList=SongList
        notifyDataSetChanged()
    }

}