package com.example.musicplayer.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongListAdapter
import com.example.musicplayer.databinding.FragmentDashboardBinding
import com.example.musicplayer.model.SongResponse
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.utils.CountDownTimerExt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_toolbar.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DashboardFragment : BaseFragment(), ServiceConnection {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

  //  private late init var speechIntent: Intent
   // private late init var speechRecognizer: SpeechRecognizer
    private lateinit var serviceIntent: Intent

    private lateinit var songAdapter: SongListAdapter

    private var keep: String = ""

    private var currPlaySong:ArrayList<SongResponse> = ArrayList()

    private lateinit var timer: CountDownTimerExt

    companion object {
        var audioList = ArrayList<SongResponse>()
        var pos = 0
        var musicService: MusicService? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(layoutInflater)


        return binding.root
    }

/*    override fun onPause() {
        super.onPause()
        *//*  if(MusicService!=null)
              mp!!.pause()*//*
    }

    override fun onResume() {
        super.onResume()
        Log.d("@Music","success")
        *//*   if(musicService!!.mp!=null)
               Log.d("@Music","success")
    //  musicService!!.mp?.start()
        else
               Log.d("@Music","fails")*//*
    }*/





    override fun setupUI() {

        pause.visibility = View.INVISIBLE

        serviceIntent = Intent(requireContext(), MusicService::class.java)
        activity?.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
        activity?.startService(serviceIntent)



        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, seek: Int, p2: Boolean) {
                start.text = timeDuration(seek)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                if (musicService?.mp != null) {
                    musicService?.mp?.seekTo(v!!.progress.toLong())
                    // timer.mMillisInFuture=player!!.contentDuration-player!!.contentPosition
                }
            }
        })

      /*  song.setOnTouchListener { _, p1 ->
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    //   Toast.makeText(requireContext(), "start ", Toast.LENGTH_SHORT).show()
                    speechRecognizer.startListening(speechIntent)
                    keep = ""
                }
                MotionEvent.ACTION_UP -> {
                    //   Toast.makeText(requireContext(), "done ", Toast.LENGTH_SHORT).show()
                    speechRecognizer.stopListening()
                }
            }
            false
        }*/


        audioList = fetchSong()
        // search()
        // speech()
        // time = thread()
        // time.start()
    }


    override fun click() {

        play.setOnClickListener {
            pause.visibility=View.VISIBLE
            play.visibility=View.INVISIBLE
          //  speechRecognizer.startListening(speechIntent)
            keep = ""

            if (musicService!!.mp == null) {
                pos = 0
                media()
            }
            musicService!!.mp?.play()


        }
        pause.setOnClickListener {
         //   speechRecognizer.stopListening()
            pause.visibility=View.VISIBLE
            play.visibility=View.INVISIBLE

            musicService!!.mp?.pause()
            activity?.stopService(Intent(requireContext(),MusicService::class.java))
        }

        next.setOnLongClickListener {
            if (musicService!!.mp != null) {
                musicService!!.mp!!.seekTo(musicService!!.mp!!.currentPosition + 10000)
            }
            true
        }

        preview.setOnLongClickListener {
            if (musicService!!.mp != null) {
                musicService!!.mp!!.seekTo(musicService!!.mp!!.currentPosition - 10000)
            }
            true
        }

        next.setOnClickListener {

            if (musicService!!.mp != null && audioList.size - 1 > pos) ++pos
            else pos = 0

            musicService!!.mp?.stop()
            media()
        }

        preview.setOnClickListener {

            if (pos == 0) pos = audioList.size - 1
            else --pos

            musicService!!.mp?.stop()
            media()
        }


    }

    fun timeDuration(duration: Int): String {

        val min: Int = duration / 1000 / 60
        val sec: Int = duration / 1000 % 60

        return if (sec < 10)
            "$min:0$sec"
        else
            "$min:$sec"
    }

    /*fun speech() {

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {

                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                    SpeechRecognizer.ERROR_SERVER -> "error from server"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Didn't understand, please try again."
                }
                Toast.makeText(requireContext(), "Song Name: " + message, Toast.LENGTH_SHORT).show()
            }

            override fun onResults(p0: Bundle?) {
                val match: ArrayList<String>? =
                    p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (match != null)
                    keep = match.get(0)
                Toast.makeText(requireContext(), "String : " + keep, Toast.LENGTH_SHORT).show()
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })
    }*/

    private fun fetchSong(): ArrayList<SongResponse> {

        val temp = ArrayList<SongResponse>()
        val uri: Uri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.RELATIVE_PATH,
            MediaStore.Audio.AudioColumns.TITLE_RESOURCE_URI,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val cur: Cursor? = context?.contentResolver?.query(
            uri,
            projection,
            null,
            null,
            Media.TITLE
        )

        if (cur != null && cur.moveToFirst()) {
            while (cur.moveToNext()) {
                val songName = cur.getString(cur.getColumnIndex(Media.TITLE))
                val url = cur.getString(cur.getColumnIndex(Media.DATA))
                val album = cur.getString(cur.getColumnIndex(Media.ALBUM))
                temp.add(SongResponse(songName, url, album))
            }
            Log.d("@Data", audioList.size.toString())
        }

        cur?.close()
        song.layoutManager = LinearLayoutManager(requireContext())
        songAdapter = SongListAdapter(temp,{

            if (it != pos) pos = it

            musicService!!.mp?.stop()
            media()
            musicService!!.mp?.play()
        },{

            currPlaySong.add(audioList[it])

        })

        song.adapter = songAdapter
        return temp
    }

    fun search() {

        val temp: ArrayList<SongResponse> = ArrayList()

        binding.toolbar.search.setOnQueryTextFocusChangeListener(object :
            SearchView.OnQueryTextListener,
            View.OnFocusChangeListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("@Tag", query!!)
                for (d in audioList) {
                    if (query.toString().contains(d.songName.toString()))
                        temp.add(d)
                }
                if (query == "" || query.isEmpty())
                    songAdapter.searchList(audioList)
                else
                    songAdapter.searchList(temp)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("@Tag", newText!!)


                for (d in audioList) {
                    if (newText.toString().contains(d.songName.toString()))
                        temp.add(d)
                }
                if (newText == "" || newText.isEmpty())
                    songAdapter.searchList(audioList)
                else
                    songAdapter.searchList(temp)
                return false
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1)
                    binding.toolbar.name.visibility = View.INVISIBLE
                else
                    binding.toolbar.name.visibility = View.VISIBLE
            }

        })

    }


    fun media() {


        val uri: Uri = Uri.parse(audioList[pos].path)
        //  var uri: Uri = Uri.parse("spotify:album:7wgrW5XyZdtk0K8PkW5A7h")
        binding.playsheet.songname.text = audioList[pos].songName//emp.songName

        serviceIntent.putExtra("Pos", pos)
        musicService?.mp = context?.let {
            ExoPlayer.Builder(it)
                .build()
                .also { exoPlayer ->
                    val mediaItem = MediaItem.fromUri(uri)
                 //   mediaItem.mediaMetadata(mediaItem)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                    exoPlayer.pauseAtEndOfMediaItems = false
                    exoPlayer.playWhenReady = true
                    exoPlayer.seekTo(0, 0)
                    exoPlayer.prepare()
                }
        }

        musicService!!.Shownotification(pos)

        musicService!!.mp?.play()


        musicService?.mp?.addListener(
            object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    when (playbackState) {
                        Player.STATE_READY -> {
                            timer = object : CountDownTimerExt(musicService!!.mp!!.contentDuration, 1000) {
                                override fun onTimerTick(millisUntilFinished: Long) {
                                    if (musicService?.mp != null) {
                                        if (musicService?.mp!!.isPlaying) {

                                            play.visibility=View.INVISIBLE
                                            pause.visibility=View.VISIBLE
                                            start.text = timeDuration(musicService!!.mp!!.contentDuration.toInt())
                                            end.text = timeDuration(musicService!!.mp!!.duration.toInt())
                                            seekBar.progress = musicService!!.mp!!.contentPosition.toInt()
                                            musicService!!.Shownotification(R.drawable.ic_pause_notification)
                                            seekBar.max = musicService?.mp!!.contentDuration.toInt()
                                        }
                                        else{
                                            play.visibility=View.VISIBLE
                                            musicService!!.Shownotification(R.drawable.ic_play_notification)
                                            pause.visibility=View.INVISIBLE
                                        }
                                    }
                                }

                                override fun onTimerFinish() {
                                    play.visibility=View.VISIBLE
                                    pause.visibility=View.INVISIBLE
                                    musicService?.mp!!.stop()
                                    musicService!!.Shownotification(R.drawable.ic_play_notification)
                                    timer.restart()
                                    if (audioList.size - 1 > pos) ++pos
                                    else pos = 0
                                    media()
                                    seekBar.progress = 0
                                }
                            }
                            timer.start()
                        }
                        Player.STATE_ENDED -> {
                            play.visibility=View.VISIBLE
                            pause.visibility=View.INVISIBLE
                            musicService!!.Shownotification(R.drawable.ic_pause_notification)
                            musicService?.mp!!.release()
                            timer.restart()
                            if (audioList.size - 1 > pos) ++pos
                            else pos = 0
                            media()
                            seekBar.progress = 0
                        }
                    }
                }
            })
    }



    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currservice()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    /*fun thread(): Thread {

        return Thread(object : Runnable {
            override fun run() {
                val han = Handler(Looper.getMainLooper())

                han.post {
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            try {

                                if (musicService!!.mp!!.isPlaying) {
                                    play.visibility = View.INVISIBLE
                                    pause.visibility = View.VISIBLE
                                    musicService!!.ShowNotification(R.drawable.ic_pause_notification)
                                } else {
                                    play.visibility = View.VISIBLE
                                    pause.visibility = View.INVISIBLE
                                    musicService!!.ShowNotification(R.drawable.ic_play_notification)
                                }
                                //    Log.d("@the", start.text.toString())
                                seekBar.progress = musicService!!.mp!!.currentPosition.toInt()
                                if (start.text == end.text) {
                                    musicService!!.mp?.stop()
                                    musicService!!.mp?.prepare()
                                    if (audioList.size - 1 > pos) ++pos
                                    else pos = 0

                                    media()
                                }
                            } catch (e: Exception) {
                                e.message
                            }
                        }
                    }, 0, 1000)
                }

            }
        })
    }*/


}


