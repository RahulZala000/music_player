package com.example.musicplayer.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongListAdapter
import com.example.musicplayer.common.Constant
import com.example.musicplayer.model.SongResponse
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.ui.fragment.DashboardFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import java.util.ArrayList


class MusicService : Service() {

    var binder=MyBinder()
    var mp: ExoPlayer? = null
    var mediasession: MediaSessionCompat?=null
    var pos:Int=0
//
    val controller = mediasession?.controller
    val mediaMetadata = controller?.metadata
    val description = mediaMetadata?.description

    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    var audiolist = ArrayList<SongResponse>()
    lateinit var tempsong: SongResponse
    lateinit var songadapter: SongListAdapter

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
        var prevPending=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var nextIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.NEXT)
        var nextPending=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var playIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.PLAY)
        var playPending=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        var exitIntent=Intent(baseContext,NotificationReceive::class.java).setAction(Constant.EXIT)
        var exitPending=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        var notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("Notify","Notify")

        var pading_intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
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
            .setContentTitle(DashboardFragment.audioList[DashboardFragment.pos].songName)//
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_notes))
            .setBadgeIconType(R.drawable.ic_music)
            .addAction(R.drawable.ic_previou, "Pre", prevPending)
            .addAction(play, "Play", playPending)
            .addAction(R.drawable.ic_next, "Next", nextPending)
            .addAction(R.drawable.ic_exit,"Exit",exitPending)
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
            // mp = DashboardFragment().mp!!
            // mp!!.isLooping = false
            mp!!.volume = 100f
        }
    }


}
