package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication:Application() {
    companion object{
        const val not_id="My_service"
        const val not_play="Play"
        const val not_pause="Pause"
        const val not_next="Next"
        const val not_pre="Preview"
    }

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val notification=NotificationChannel(not_id,"Now Playing Song",NotificationManager.IMPORTANCE_HIGH)
            notification.description="show the playing song"
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notification)
        }
    }
}