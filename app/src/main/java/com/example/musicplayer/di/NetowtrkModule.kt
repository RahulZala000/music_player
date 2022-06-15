package com.example.musicplayer.di

import android.content.Context
import androidx.room.Room
import com.example.musicplayer.api.MusicApi
import com.example.musicplayer.common.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.dubaistudent.data.database.SpotifyDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetowtrkModule {

    @Provides
    @Singleton
    fun Httpclient():OkHttpClient
    {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun RetrofitInstance(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun ApiService(retrofit: Retrofit): MusicApi {
        return retrofit.create(MusicApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNotesDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, SpotifyDatabase::class.java,"SongData" )
        .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideNoteDao(db: SpotifyDatabase) = db.optiXDao()
}