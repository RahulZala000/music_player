package com.example.musicplayer.data

import com.example.musicplayer.model.SongResponse
import es.dubaistudent.data.database.SpotifyDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val optiXDao: SpotifyDao
) {

    fun getParentCategoriesObserve(): Flow<List<SongResponse>> {
        return optiXDao.getParentCategoriesObserve()
    }



}