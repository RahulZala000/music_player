package com.example.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.musicplayer.MyApplication
import com.example.musicplayer.R
import com.example.musicplayer.common.Constant
import com.example.musicplayer.ui.fragment.DashboardFragment
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

class NotificationReceive : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent?.action) {
            Constant.PRE -> {
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show()
            }
            Constant.PLAY -> {
                if (DashboardFragment.musicService!!.mp!!.isPlaying) Pausemusic()
                else Playmusic()
              //  if(DashboardFragment.musicService==null) Playmusic()
            }

            Constant.NEXT -> {
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show()
            }
            Constant.EXIT -> {
                DashboardFragment.musicService!!.stopForeground(true)
                DashboardFragment.musicService = null
                exitProcess(1)
            }
        }
    }

    private fun Playmusic() {

        DashboardFragment.musicService!!.Shownotification(R.drawable.ic_pause_notification)
        DashboardFragment.musicService!!.mp!!.start()
    }

    private fun Pausemusic() {

        DashboardFragment.musicService!!.Shownotification(R.drawable.ic_play_notification)
        DashboardFragment.musicService!!.mp!!.pause()
    }


}