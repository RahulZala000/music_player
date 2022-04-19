package com.example.musicplayer.ui.fragment

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.media.AudioAttributes
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
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.android.synthetic.main.dashboard_toolbar.*
import kotlinx.android.synthetic.main.dashboard_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import kotlinx.android.synthetic.main.layout_playsheet.view.*
import java.util.*
import kotlin.properties.Delegates


class DashboardFragment : BaseFragment(),ServiceConnection {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

     var mp: MediaPlayer?=null
    lateinit var speechintent:Intent
    lateinit var speechRecognizer: SpeechRecognizer

    var audiolist=ArrayList<SongResponse>()
    lateinit var tempsong:SongResponse
    lateinit var songadapter:SonglistAdapter
    var pos=0
    var keep:String=""
//    lateinit var runnable:Runnable
    lateinit var time:Thread
    var musicService:MusicService?=null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding=FragmentDashboardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun setupUI() {

        pause.visibility=View.INVISIBLE

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, seek: Int, p2: Boolean) {
                    start.text=timeduration(seek)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                mp!!.seekTo(v!!.progress)
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

        fetch_song()
        search()
        speech()
        time=thread()
        time.start()
    }

    override fun click()
    {
       play.setOnClickListener{
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
           speechRecognizer.startListening(speechintent)
             keep=""

           if(mp==null){
               pos=0
               tempsong=audiolist.get(pos)
               mp=media(tempsong)
           }
          activity?.startService(Intent(requireContext(),MusicService::class.java))
            mp?.start()


        }
        pause.setOnClickListener{
            play.visibility=View.VISIBLE
            pause.visibility=View.INVISIBLE
           speechRecognizer.stopListening()
            mp?.pause()
            activity?.stopService(Intent(requireContext(),MusicService::class.java))
        }

        next.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                if(mp!=null) {
                    mp!!.seekTo(mp!!.currentPosition+10000)
                }
                return true
            }
        })

        preview.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                if(mp!=null) {
                    mp!!.seekTo(mp!!.currentPosition-10000)
                }
                return true
            }
        })

        next.setOnClickListener{

            if (mp!=null && audiolist.size-1>pos) ++pos
            else pos=0

            mp?.stop()
            mp?.release()

            tempsong=audiolist.get(pos)
            mp=media(tempsong)
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
        }

        preview.setOnClickListener {

            if (pos==0) pos=audiolist.size-1
            else --pos

            mp?.stop()
            mp?.release()

            tempsong=audiolist.get(pos)
            mp=media(tempsong)
            mp?.start()
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
        }


    }

    fun timeduration(duration:Int):String {

        var min:Int=duration/1000/60
        var sec:Int=duration/1000%60



        if (sec<10 )
            return ""+min + ":0"+sec
        else
            return ""+min + ":"+sec
    }

    fun speech(){

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
                Toast.makeText(requireContext(),"Song Name: "+message,Toast.LENGTH_SHORT).show()
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

   fun fetch_song()
    {

        var suri:Uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.RELATIVE_PATH,
            MediaStore.Audio.AudioColumns.TITLE_RESOURCE_URI,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        var cur: Cursor? =context?.contentResolver?.query(suri,null,null, null,null)

            if(cur!=null && cur.moveToFirst())
            {
                var songtitle:Int= cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                var file: Int =cur.getColumnIndex(MediaStore.Audio.Media.DATA)
                var album:Int=cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                while (cur.moveToNext())
                {
                    var songname:String=cur.getString(songtitle)
                    var ur=cur.getString(file)
                    val falbum: String = cur.getString(album)

                    audiolist.add(SongResponse(songname,ur,falbum))

                }

               Log.d("@Data",audiolist.size.toString())
            }

        cur?.close()
        song.layoutManager=LinearLayoutManager(requireContext())
        songadapter=SonglistAdapter(audiolist,object : AdapterClickListerner {

            override fun onItemClick(view: View?, Pos: Int, Song: Any?) {
                tempsong = Song as SongResponse
                if (Pos != pos) pos = Pos
                else pos=0


                    mp?.stop()
                    mp?.prepare()
                    mp = media(tempsong)
                    pause.visibility = View.VISIBLE
                    play.visibility = View.INVISIBLE
                    mp?.start()
                }
        })
        song.adapter=songadapter


    }

     fun search(){

         var temp:ArrayList<SongResponse> = ArrayList()

        search_song.setOnQueryTextFocusChangeListener(object : SearchView.OnQueryTextListener,
            View.OnFocusChangeListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("@Tag",query.toString())
                for(d in audiolist)
                {
                    if(query.toString()?.contains(d.songname.toString()))
                        temp.add(d)
                }
                if(query=="" || query!!.isEmpty())
                    songadapter.searchlist(audiolist)
                else
                    songadapter.searchlist(temp)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("@Tagg",newText.toString())


                for(d in audiolist)
                {
                    if(newText.toString()?.contains(d.songname.toString()))
                        temp.add(d)
                }
                if(newText=="" || newText!!.isEmpty())
                    songadapter.searchlist(audiolist)
                else
                    songadapter.searchlist(temp)
                return false
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {
                if(p1)
                   toolbar.name.visibility =View.INVISIBLE
                else
                    toolbar.name.visibility=View.VISIBLE
            }

        })

     }






    fun media(temp:SongResponse):MediaPlayer
    {
        var uri:Uri=Uri.parse(temp.path)
        playsheet.songname.text=temp.songname

        mp=MediaPlayer.create(requireContext(),uri)
        activity?.startService(Intent(requireContext(),MusicService::class.java))
        seekBar.max=mp!!.duration
        start.text=timeduration(mp!!.currentPosition)
        end.text=timeduration(mp!!.duration)
        seekBar.progress=mp!!.currentPosition

        mp!!.start()





       // time= Thread(runnable)

        return mp!!
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder=p1 as MusicService.MyBinder
        musicService=binder.currservice()
        musicService!!.Shownotification()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
       musicService=null
    }

    fun thread():Thread {

       return  Thread(object : Runnable {
            override fun run() {
                var han = Handler(Looper.getMainLooper())

                han.post(object : Runnable {
                    override fun run() {

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                try {

                                    if (mp!!.isPlaying) {
                                        play.visibility = View.INVISIBLE
                                        pause.visibility = View.VISIBLE
                                    } else {
                                        play.visibility = View.VISIBLE
                                        pause.visibility = View.INVISIBLE
                                    }
                                    Log.d("@the", start.text.toString())
                                    seekBar.progress = mp!!.currentPosition
                                    if (start.text == end.text) {
                                        mp?.stop()
                                        mp?.prepare()
                                        if (audiolist.size - 1 > pos) ++pos
                                        else pos = 0

                                        tempsong = audiolist.get(pos)
                                        mp = media(tempsong)
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


