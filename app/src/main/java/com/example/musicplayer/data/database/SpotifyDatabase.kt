package es.dubaistudent.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicplayer.model.SongResponse

@Database(
    entities = [ SongResponse::class],
    version = 1
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun optiXDao(): SpotifyDao
}