package com.example.musicplayer.model

import java.time.Duration

data class SongResponse(
    var songname:String?,
    var path:String?,
    var album:String,
    var duration: Long=0
)

fun Timeformate(duration: Long){

}
