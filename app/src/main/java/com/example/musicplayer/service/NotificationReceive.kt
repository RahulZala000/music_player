package com.example.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast
import com.example.musicplayer.MyApplication
import com.example.musicplayer.R
import com.example.musicplayer.common.Constant
import com.example.musicplayer.ui.fragment.DashboardFragment
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

class NotificationReceive:BroadcastReceiver() {

    var mp=DashboardFragment()
    override fun onReceive(context: Context, intent: Intent) {
        when(intent?.action){
            Constant.PRE->{ DashboardFragment.musicService!!.pos-=1 }
            Constant.PLAY->{ if(DashboardFragment.musicService!!.mp!!.isPlaying) Pausemusic()
                else Playmusic()
            }

            Constant.NEXT->{ Toast.makeText(context,"Play",Toast.LENGTH_SHORT).show() }
            Constant.EXIT-> {
                DashboardFragment.musicService!!.stopForeground(true)
                DashboardFragment.musicService=null
                exitProcess(1)
            }

            }

        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            DashboardFragment.musicService!!.mp!!.pause()
        }

        }

    private fun Playmusic() {

        /*if(DashboardFragment.musicService!!.mp==null)
        {
            DashboardFragment.pos=0
            mp.media()

        }*/
      //  else{
            DashboardFragment.musicService!!.Shownotification(R.drawable.ic_pause_notification)
            DashboardFragment.musicService!!.mp!!.start()
      //  }

    }
    private fun Pausemusic() {
        DashboardFragment.musicService!!.Shownotification(R.drawable.ic_play_notification)
        DashboardFragment.musicService!!.mp!!.pause()
    }


}