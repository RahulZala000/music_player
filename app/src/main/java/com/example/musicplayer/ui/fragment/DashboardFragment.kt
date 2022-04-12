package com.example.musicplayer.ui.fragment

import android.content.Intent
import android.database.Cursor
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.state.ToggleableState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.adapter.SonglistAdapter
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.FragmentDashboardBinding
import com.example.musicplayer.model.SongResponse
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*
import kotlinx.android.synthetic.main.layout_playsheet.view.*
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

    var mp: MediaPlayer?=null
    lateinit var speechintent:Intent
    lateinit var speechRecognizer: SpeechRecognizer

    var audiolist=ArrayList<SongResponse>()
    lateinit var tempsong:SongResponse
    lateinit var songadapter:SonglistAdapter
    var pos:Int=0
    var keep:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding=FragmentDashboardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun setupUI() {
        if(mp!=null) {
            play.visibility = View.INVISIBLE
            pause.visibility=View.VISIBLE
        }
        else{
            play.visibility = View.VISIBLE
            pause.visibility=View.INVISIBLE
        }

        speech()

        song.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Toast.makeText(requireContext(), "start ", Toast.LENGTH_SHORT).show()
                        speechRecognizer.startListening(speechintent)
                        keep = ""
                    }
                    MotionEvent.ACTION_UP -> {
                        Toast.makeText(requireContext(), "done ", Toast.LENGTH_SHORT).show()
                        speechRecognizer.stopListening()
                    }
                }
                return false
            }

        })

       fetch_song()
        click()


       // search()


    }

    fun click()
    {
       play.setOnClickListener{
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
     //      speechRecognizer.startListening(speechintent)
          //   keep=""

           if(mp==null){
               tempsong=audiolist.get(pos)
               playsheet.songname.text= tempsong!!.songname
               mp=media(tempsong)
           }
            mp?.start()



        }
        pause.setOnClickListener{
            play.visibility=View.VISIBLE
            pause.visibility=View.INVISIBLE
       //     speechRecognizer.stopListening()
            mp?.pause()


        }
        next.setOnClickListener{

            if (mp!=null && audiolist.size-1>pos)
                ++pos
            else
                pos=0

            mp?.stop()
            mp?.release()

            tempsong=audiolist.get(pos)
            mp=media(tempsong)
           mp?.start()
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
        }

        preview.setOnClickListener {

            if (mp!=null && audiolist.size-1<pos)
                --pos
            else
                pos=audiolist.size-1

            mp?.stop()
            mp?.release()

            tempsong=audiolist.get(pos)
            mp=media(tempsong)
            mp?.start()
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
        }
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
                //    val artist: String = cur.getString(3)

                    audiolist.add(SongResponse(songname,ur,falbum))

                }

               Log.d("@Data",audiolist.size.toString())
            }

        cur?.close()
        song.layoutManager=LinearLayoutManager(requireContext())
        songadapter=SonglistAdapter(audiolist,object : AdapterClickListerner {

            override fun onItemClick(view: View?, Pos: Int, Song: Any?) {
                tempsong = Song as SongResponse
                pos=Pos

                if(mp!=null)
                {
                    pause.visibility=View.INVISIBLE
                    play.visibility=View.VISIBLE
                }

                mp?.stop()
                mp?.release()
                mp= media(tempsong)
                pause.visibility=View.VISIBLE
                play.visibility=View.INVISIBLE
                mp?.start()
            }
        })
        song.adapter=songadapter


    }



    /* fun search(){

         var temp:ArrayList<String> = ArrayList()

         search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(query: String?): Boolean {
                for(d in audioList)
                {
                    if(query.toString() in d)
                        temp.add(d)
                }
                 return false
             }

             override fun onQueryTextChange(newt: String?): Boolean {
                 for(d in audioList)
                 {
                     if(newt.toString() in d)
                         temp.add(d)
                 }
                 return false
             }
         })
     }*/

    /* fun fliterlist(list:String)
     {


             for(d in audioList)
             {
                 if(list in d)
                     temp.add(d)
             }



         if(songadapter.songlist.size==0)
             songadapter.songlist=audioList
         else
             songadapter.songlist=temp

     }
 */

    fun media(temp:SongResponse):MediaPlayer
    {
        var uri:Uri=Uri.parse(temp.path)
        playsheet.songname.text=temp.songname
        mp= MediaPlayer.create(requireContext(),uri)
        return mp!!
    }
}

