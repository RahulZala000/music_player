package com.example.musicplayer.model

data class SongResponse(
    var songName:String?,
    var path:String?,
    var album:String,
    var duration: Long=0
)
