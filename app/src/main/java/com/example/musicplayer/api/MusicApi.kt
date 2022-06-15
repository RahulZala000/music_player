package com.example.musicplayer.api


import com.example.musicplayer.model.ProductModel
import com.example.musicplayer.model.SearchSongRenspose
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MusicApi {

    companion object
    {
        const val SEARCH="search/"
    }

    @GET(SEARCH)
    @Headers("X-RapidAPI-Host:spotify23.p.rapidapi.com","X-RapidAPI-Key:43ecac8d72msh34dba616a26958fp1ce5e4jsnc8fa9e141ae7")
    suspend fun SearchSong(
        @Query("q") sname:String,
        @Query("type")type:String,
        @Query("offset") offset:Int,
        @Query("limit") limit:Int,
        @Query("numberOfTopResults")res:Int
    ):Response<SearchSongRenspose>
}