package com.example.musicplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SonglistAdapter
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.common.Constant
import com.example.musicplayer.common.Constant.MY_EMPTY_MEDIA_ROOT_ID
import com.example.musicplayer.common.Constant.MY_MEDIA_ROOT_ID
import com.example.musicplayer.model.SongResponse
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.ui.fragment.DashboardFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import java.util.ArrayList


class MusicService : Service() {

    var binder=MyBinder()
    var mp: MediaPlayer? = null
    var mediasession: MediaSessionCompat?=null
    var pos:Int=0
//
    val controller = mediasession?.controller
    val mediaMetadata = controller?.metadata
    val description = mediaMetadata?.description

    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    var audiolist = ArrayList<SongResponse>()
    lateinit var tempsong: SongResponse
    lateinit var songadapter: SonglistAdapter

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class MyBinder: Binder() {
        fun currservice():MusicService{
            return this@MusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        initMusic()

     /*   mediasession = MediaSessionCompat(baseContext, LOG_TAG).apply {

            // Enable callbacks from MediaButtons and TransportControls
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
*/
            // MySessionCallback() has methods that handle callbacks from a media controller
         //   setCallback(MySessionCallback())

            // Set the session's token so that client activities can communicate with it.
         //   setSessionToken(sessionToken)
      //  }

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

      /*  if (mp != null) {
            if (mp!!.isPlaying)
                mp!!.stop()
            else
                mp!!.start()
        }*/


        return START_STICKY
    }

    fun Shownotification(play: Int) {

        var prevIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.PRE)
        var prevpeding=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var nextIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.NEXT)
        var nextpeding=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var playIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.PLAY)
        var playpeding=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        var exitIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.EXIT)
        var exitpeding=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)



        var notification_intent = Intent(this, MainActivity::class.java)
        notification_intent.putExtra("Notify","Notify")

        var pading_intent = PendingIntent.getActivity(this, 0, notification_intent, 0)
        var s= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("My_service", "My Background Service")
        }
        else
        {
            ""
        }
        val notificationBuilder = NotificationCompat.Builder(this, s )
        val notification = notificationBuilder.setOngoing(true)
            .setContentInfo(Constant.NOTIFICATION)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle(DashboardFragment.audiolist[DashboardFragment.pos].songname)//
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_notes))
            .setBadgeIconType(R.drawable.ic_music)
            .addAction(R.drawable.ic_previou, "Pre", prevpeding)
            .addAction(play, "Play", playpeding)
            .addAction(R.drawable.ic_next, "Next", nextpeding)
            .addAction(R.drawable.ic_exit,"Exit",exitpeding)
            .setContentIntent(pading_intent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediasession?.sessionToken)
            )
            .setPriority(PRIORITY_MIN)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            // .setCategory(Notification.EXTRA_MEDIA_SESSION)
            .build()
        startForeground(Constant.NOTIFICATION_ID, notification)

    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_MIN)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun initMusic() {

        if (mp != null) {
          //  mp = DashboardFragment().mp!!
            mp!!.isLooping = false

            mp!!.setVolume(100f, 100f)
        }
    }


}
