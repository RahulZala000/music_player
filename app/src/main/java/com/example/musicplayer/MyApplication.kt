package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.common.Constant
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.managers.ApplicationComponentManager

@HiltAndroidApp
class MyApplication:Application() {


    override fun onCreate() {
        super.onCreate()

          /*  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val notification=NotificationChannel(Constant.CHANNAL_ID,"Now Playing Song",NotificationManager.IMPORTANCE_HIGH)
            notification.description="show the playing song"
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notification)
        }*/
    }
}