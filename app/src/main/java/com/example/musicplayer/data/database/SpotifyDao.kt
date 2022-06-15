package es.dubaistudent.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.musicplayer.model.SongResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyDao {



    @Query("SELECT * FROM Song ")
    fun getParentCategoriesObserve(): Flow<List<SongResponse>>

    /*@Query("DELETE FROM Song")
    suspend fun deleteAllCategories()*/
/*

    // for sub category
    @Query("SELECT * FROM Song WHERE songname = songname")
    suspend fun getSubCategory(id:Int) : List<SongResponse>?
*/


}
















