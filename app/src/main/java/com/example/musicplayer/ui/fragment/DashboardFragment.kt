package com.example.musicplayer.ui.fragment

import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.common.AdapterClickListerner
import com.example.musicplayer.databinding.FragmentDashboardBinding
import com.example.musicplayer.model.SongResponse
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_playsheet.*


class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

    lateinit var mediaPlayer: MediaPlayer



    var audioList= ArrayList<SongResponse>()
   //  lateinit var arrayAdapter: ArrayAdapter<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pause.visibility=View.GONE
        play.visibility=View.VISIBLE

        fetch_song()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding=FragmentDashboardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun setupUI() {

        click()
    }

    fun click()
    {
        play.setOnClickListener{
            play.visibility=View.GONE
            pause.visibility=View.VISIBLE
        }
        pause.setOnClickListener{
            play.visibility=View.VISIBLE
            pause.visibility=View.GONE
        }
    }

    fun fetch_song()
    {

     // audioList!!.clear()
        var suri:Uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var cur: Cursor? =context?.contentResolver?.query(suri,null,null,null,null)

            if(cur!=null && cur.moveToFirst())
            {
                var songtitle:Int= cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
//                var songsources:Int=cur.getColumnIndex(MediaStore.Audio.Media.TITLE_RESOURCE_URI)
                while (cur.moveToNext())
                {
                    var songname:String=cur.getString(songtitle)
//                    var songurl:String=cur.getString(songsources)
                    audioList.add(SongResponse(songname,""))
                }
             //   audioList.add(songtitle.toString())
                Log.d("@Data",audioList!!.size.toString())
            }

        cur?.close()
        song.layoutManager=LinearLayoutManager(requireContext())
        song.adapter=SongAdapter(audioList,object : AdapterClickListerner{
            override fun onItemClick(view: View?, pos: Int, song: Any?) {
              //  mediaPlayer.setDataSource()
                Toast.makeText(requireContext(),"Song Name: "+song,Toast.LENGTH_SHORT).show()
            }

        })



      //  songadapter?.notifyDataSetChanged()

    }


}