package com.example.musicplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SongResponse(
    var songname:String?,
    var path:String?,
    var album:String,
    var duration: Long=0
)
