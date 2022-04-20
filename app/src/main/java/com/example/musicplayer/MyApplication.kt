package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.musicplayer.common.Constant

class MyApplication:Application() {
    companion object{

    }

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val notification=NotificationChannel(Constant.CHANNAL_ID,"Now Playing Song",NotificationManager.IMPORTANCE_HIGH)
            notification.description="show the playing song"
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notification)
        }
    }
}