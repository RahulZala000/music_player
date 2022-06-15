package com.example.musicplayer.model

data class AlbumOfTrack(
    val coverArt: CoverArt,
    val id: String,
    val name: String,
    val sharingInfo: SharingInfo,
    val uri: String
)