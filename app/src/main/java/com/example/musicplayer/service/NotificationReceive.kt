package com.example.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.musicplayer.MyApplication
import kotlin.coroutines.coroutineContext

class NotificationReceive:BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent) {
        when(intent?.action){
            MyApplication.not_pause->{
               // notificationManager.cancelAll();
            }
        }
    }
}