package com.example.musicplayer.ui.fragment

import android.content.ComponentName
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.adapter.SeachSongAdapater
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.model.Item
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.utils.NetworkResult
import com.example.musicplayer.viewmodel.SearchSongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() ,ServiceConnection,SeachSongAdapater.songdata{

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    var mp:MediaPlayer?=null

    lateinit var viewmodel:SearchSongViewModel
    var searchsong:String=""
    lateinit var adapter:SeachSongAdapater
    companion object {
         var musicService: MusicService?=null
        lateinit var item:ArrayList<Item>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel=ViewModelProvider(this)[SearchSongViewModel::class.java]
        setobserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    fun setobserver(){
        lifecycleScope.launchWhenCreated {
            viewmodel.mySearchSong.observe(this@HomeFragment) { it ->
                when (it) {
                    is NetworkResult.Success -> {
                        it.data.let {
                            if (it != null) {
                                  Log.d("@D",it.albums.totalCount.toString())
                                  adapter.Seachsong(it.albums.items)
                                item= it.albums.items as ArrayList<Item>
                            }
                        }
                    }
                }
            }
        }
    }


    override fun click() {



        binding.toolbar.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,View.OnFocusChangeListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchsong=p0!!
                Log.d("@seach",p0!!)
                callapi(1)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
               Log.d("@seach",p0!!)
                return false
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1)
                    binding.toolbar.name.visibility = View.INVISIBLE
                else
                    binding.toolbar.name.visibility = View.VISIBLE
            }

        })

        binding.offline.setOnClickListener {
           navigateTo(HomeFragmentDirections.actionHomeFragmentToDashboardFragment())
        }
    }


    override fun setupUI() {


        setupRecycleview()

    }


    fun callapi(no:Int)
    {
        when(no) {
            1 -> {
                viewmodel.getSong(searchsong, "multi", 0, 10, 5)
            }
        }
    }

    fun setupRecycleview(){
        binding.song.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter= SeachSongAdapater(this@HomeFragment)

        binding.song.adapter=adapter
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currservice()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    override fun songd(data: Item) {
        var uri= Uri.parse(data.data.uri)
       // if(musicService !=null) {
      mp = MediaPlayer.create(context, uri)
        mp!!.start()
     //   }
    }


}