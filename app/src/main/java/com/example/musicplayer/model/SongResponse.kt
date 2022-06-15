package com.example.musicplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Song")
data class SongResponse(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var songname:String?,
    var path:String?,
    var album:String,
    var duration: Long=0
)
