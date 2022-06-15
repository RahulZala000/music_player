package com.example.musicplayer.model

data class SearchSongRenspose(
    val albums: Albums,
    val artists: Artists,
    val episodes: Episodes,
    val genres: Genres,
    val playlists: Playlists,
    val podcasts: Podcasts,
    val topResults: TopResults,
    val tracks: Tracks,
    val users: Users
)