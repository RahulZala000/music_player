package com.example.musicplayer.service

import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.musicplayer.MyApplication
import com.example.musicplayer.R


class MusicService: Service(){

    var mybinder=MyBinder()
    var mp:MediaPlayer?=null
    lateinit var mediasession:MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder? {
        mediasession= MediaSessionCompat(baseContext,"MyMusic")
      return mybinder
    }

    inner class MyBinder:Binder(){
        fun currentservice():MusicService{
            return this@MusicService
        }
    }


    fun notificationshow(){
        val notification=NotificationCompat.Builder(baseContext,MyApplication.not_id)
            .setContentTitle("My Song")
            .setSmallIcon(R.drawable.ic_play)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediasession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_previou,"Pre",null)
            .addAction(R.drawable.ic_play,"Play",null)
            .addAction(R.drawable.ic_pause,"Pause",null)
            .addAction(R.drawable.ic_next,"Next",null)
            .build()

        startForeground(31,notification)

    }
}