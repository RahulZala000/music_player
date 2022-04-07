package com.example.musicplayer.ui.fragment

import android.R.string
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.musicplayer.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding: FragmentDashboardBinding
        get() = _binding!!

   // val audiolist: ArrayList<string> = ArrayList()
    lateinit var audioList:ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding=FragmentDashboardBinding.inflate(layoutInflater)
        fetch_song()
        return binding.root
    }

    override fun setupUI() {

    }

    fun fetch_song(){

        val proj = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME)

        val audioCursor: Cursor? = context?.getContentResolver()?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            proj,
            null,
            null,
            null
        )

        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    val audioIndex =
                        audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                    audioList.add(audioCursor.getString(audioIndex))
                } while (audioCursor.moveToNext())
            }
        }

        audioCursor?.close();


    //    ArrayAdapter<string> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1, audioList);
    //    audioView.setAdapter(adapter);

       // val adapter: ArrayAdapter<string> = ArrayAdapter<Any?>(this, R.layout.simple_list_item_1, R.id.text1, audioList)
      //  song.setAdapter(adapter) as ArrayList<String>
    }
}