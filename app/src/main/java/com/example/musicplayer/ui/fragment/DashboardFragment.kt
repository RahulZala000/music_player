package com.example.musicplayer.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.SonglistAdapter
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.FragmentDashboardBinding
import com.example.musicplayer.model.SongResponse
import com.example.musicplayer.service.MusicService
import kotlinx.android.synthetic.main.dashboard_toolbar.*
import kotlinx.android.synthetic.main.dashboard_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import kotlinx.android.synthetic.main.layout_playsheet.view.*
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : BaseFragment(), ServiceConnection {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

    // var mp: MediaPlayer?=null
    lateinit var speechintent: Intent
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var serviceintent: Intent


    lateinit var tempsong: SongResponse
    lateinit var songadapter: SonglistAdapter

    var keep: String = ""
    var pos =0
    lateinit var time: Thread
    var musicService: MusicService? = null

    companion object {
        var audiolist = ArrayList<SongResponse>()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onStart() {
        super.onStart()


    }


    override fun onPause() {
        super.onPause()
        /*  if(MusicService!=null)
              mp!!.pause()*/
    }

    override fun onResume() {
        super.onResume()
        Log.d("@Music","success")
        /*   if(musicService!!.mp!=null)
               Log.d("@Music","success")
    //  musicService!!.mp?.start()
        else
               Log.d("@Music","fails")*/
    }

    override fun setupUI() {

        pause.visibility = View.INVISIBLE

        serviceintent = Intent(requireContext(), MusicService::class.java)
        Log.d("@Ser", "service is start")
        activity?.bindService(serviceintent, this, Context.BIND_AUTO_CREATE)
        activity?.startService(serviceintent)



        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, seek: Int, p2: Boolean) {
                start.text = timeduration(seek)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                musicService!!.mp!!.seekTo(v!!.progress)
            }
        })

        song.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //   Toast.makeText(requireContext(), "start ", Toast.LENGTH_SHORT).show()
                        speechRecognizer.startListening(speechintent)
                        keep = ""
                    }
                    MotionEvent.ACTION_UP -> {
                        //   Toast.makeText(requireContext(), "done ", Toast.LENGTH_SHORT).show()
                        speechRecognizer.stopListening()
                    }
                }
                return false
            }
        })

        audiolist=fetch_song()
        search()
        speech()
        time = thread()
        time.start()
    }

    fun stopser() {
        activity?.stopService(Intent(requireContext(), MusicService::class.java))
    }

    override fun click() {
        play.setOnClickListener {
            play.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE
            speechRecognizer.startListening(speechintent)
            keep = ""

            if (musicService!!.mp == null) {
                pos = 0
                media()
            }
          //  activity?.startService(serviceintent)
            musicService!!.mp?.start()


        }
        pause.setOnClickListener {
            play.visibility = View.VISIBLE
            pause.visibility = View.INVISIBLE
            speechRecognizer.stopListening()
            musicService!!.mp?.pause()
            //   activity?.stopService(Intent(requireContext(),MusicService::class.java))
        }

        next.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                if (musicService!!.mp != null) {
                    musicService!!.mp!!.seekTo(musicService!!.mp!!.currentPosition + 10000)
                }
                return true
            }
        })

        preview.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                if (musicService!!.mp != null) {
                    musicService!!.mp!!.seekTo(musicService!!.mp!!.currentPosition - 10000)
                }
                return true
            }
        })

        next.setOnClickListener {

            if (musicService!!.mp != null && audiolist.size - 1 > pos) ++pos
            else pos = 0

            musicService!!.mp?.stop()
            musicService!!.mp?.reset()
            media()
            play.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE
        }

        preview.setOnClickListener {

            if (pos == 0) pos = audiolist.size - 1
            else --pos

            musicService!!.mp?.stop()
            musicService!!.mp?.reset()
            media()
            musicService!!.mp?.start()
            play.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE
        }


    }

    fun timeduration(duration: Int): String {

        var min: Int = duration / 1000 / 60
        var sec: Int = duration / 1000 % 60

        if (sec < 10)
            return "" + min + ":0" + sec
        else
            return "" + min + ":" + sec
    }

    fun speech() {

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechintent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechintent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

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
                var message = ""
                message = when (error) {
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
                var match: ArrayList<String>? =
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
    }

    fun fetch_song(): ArrayList<SongResponse> {

        var temp=ArrayList<SongResponse>()
        var suri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.RELATIVE_PATH,
            MediaStore.Audio.AudioColumns.TITLE_RESOURCE_URI,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        var cur: Cursor? = context?.contentResolver?.query(
            suri,
            projection,
            null,
            null,
            MediaStore.Audio.Media.TITLE
        )

        if (cur != null && cur.moveToFirst()) {
            while (cur.moveToNext()) {
                var songname = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE))
                var ur = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA))
                val falbum = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                temp.add(SongResponse(songname, ur, falbum))

            }
            Log.d("@Data", audiolist.size.toString())
        }

        cur?.close()
        song.layoutManager = LinearLayoutManager(requireContext())
        songadapter = SonglistAdapter(temp, object : AdapterClickListerner {

            override fun onItemClick(view: View?, Pos: Int) {

                if (Pos != pos) pos = Pos
                else pos = 0


                musicService!!.mp?.stop()
                musicService!!.mp?.prepare()
                media()
                pause.visibility = View.VISIBLE
                play.visibility = View.INVISIBLE
                musicService!!.mp?.start()
            }
        })
        song.adapter = songadapter

        return temp

    }

    fun search() {

        var temp: ArrayList<SongResponse> = ArrayList()

        search_song.setOnQueryTextFocusChangeListener(object : SearchView.OnQueryTextListener,
            View.OnFocusChangeListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("@Tag", query.toString())
                for (d in audiolist) {
                    if (query.toString()?.contains(d.songname.toString()))
                        temp.add(d)
                }
                if (query == "" || query!!.isEmpty())
                    songadapter.searchlist(audiolist)
                else
                    songadapter.searchlist(temp)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("@Tagg", newText.toString())


                for (d in audiolist) {
                    if (newText.toString()?.contains(d.songname.toString()))
                        temp.add(d)
                }
                if (newText == "" || newText!!.isEmpty())
                    songadapter.searchlist(audiolist)
                else
                    songadapter.searchlist(temp)
                return false
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1)
                    toolbar.name.visibility = View.INVISIBLE
                else
                    toolbar.name.visibility = View.VISIBLE
            }

        })

    }


    fun media() {
        var uri: Uri = Uri.parse(audiolist[pos].path)
       binding.playsheet.songname.text = audiolist[pos].songname//emp.songname

        serviceintent.putExtra("Pos", pos)
        musicService!!.mp = MediaPlayer.create(requireContext(), uri)
        serviceintent.putExtra("Pos", pos)
        seekBar.max = musicService?.mp!!.duration
        start.text = timeduration(musicService!!.mp!!.currentPosition)
        end.text = timeduration(musicService!!.mp!!.duration)
        seekBar.progress = musicService!!.mp!!.currentPosition
        musicService!!.mp!!.start()
    }


    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currservice()
        /*if(pos!=0)
        media()*/
        musicService!!.Shownotification(pos)

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    fun thread(): Thread {

        return Thread(object : Runnable {
            override fun run() {
                var han = Handler(Looper.getMainLooper())

                han.post(object : Runnable {
                    override fun run() {

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                try {

                                    if (musicService!!.mp!!.isPlaying) {
                                        play.visibility = View.INVISIBLE
                                        pause.visibility = View.VISIBLE
                                    } else {
                                        play.visibility = View.VISIBLE
                                        pause.visibility = View.INVISIBLE
                                    }
                                    Log.d("@the", start.text.toString())
                                    seekBar.progress = musicService!!.mp!!.currentPosition
                                    if (start.text == end.text) {
                                        musicService!!.mp?.stop()
                                        musicService!!.mp?.prepare()
                                        if (audiolist.size - 1 > pos) ++pos
                                        else pos = 0

                                        media()
                                    }
                                } catch (e: Exception) {
                                    e.message
                                }
                            }
                        }, 0, 1000)
                    }

                })

            }
        })
    }
}


