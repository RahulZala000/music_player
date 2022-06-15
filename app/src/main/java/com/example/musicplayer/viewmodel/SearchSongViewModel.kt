package com.example.musicplayer.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.R
import com.example.musicplayer.common.Constant
import com.example.musicplayer.data.Repository
import com.example.musicplayer.model.ProductModel
import com.example.musicplayer.model.SearchSongRenspose
import com.example.musicplayer.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SearchSongViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {
    var mContext = application

    var mySearchSong: MutableLiveData<NetworkResult<SearchSongRenspose>> = MutableLiveData()

    fun getSong(s:String, type:String, offerset:Int, limit:Int, res:Int) = viewModelScope.launch {
        SearchSong(s,type,offerset,limit,res)
    }

    private suspend fun SearchSong(s:String, type:String, offerset:Int, limit:Int, res:Int) {

        mySearchSong.value = NetworkResult.Loading()
        if (isOnline(context)) {
            try {
               val response = repository.remote.getSong(s,type,offerset,limit,res)
                mySearchSong.value = handleResponse(response)!!
            } catch (e: Exception) {
                mySearchSong.value =
                    NetworkResult.Error(
                        mContext.getString(R.string.wrong)
                                + (if (Constant.SHOW_RESPONSE_LOCAL_ERROR) "\n" + e.localizedMessage else "")
                    )
                Timber.e(e.localizedMessage)
            }
        } else {
            mySearchSong.value =
                NetworkResult.Error(mContext.getString(R.string.offline))
        }
    }

}