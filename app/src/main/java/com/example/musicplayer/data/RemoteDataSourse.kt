package com.example.musicplayer.data

import android.app.Application
import com.example.musicplayer.api.MusicApi
import com.example.musicplayer.model.ProductModel
import com.example.musicplayer.model.SearchSongRenspose
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val api: MusicApi,
    application: Application,
) {

    suspend fun getSong(s:String,type:String,offerset:Int,limit:Int,res:Int):Response<SearchSongRenspose>
    {
        return api.SearchSong(s,type,offerset,limit,res)
    }

}