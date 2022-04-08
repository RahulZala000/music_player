package com.example.musicplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding


    /* var audioList:ArrayList<String>?=null
     lateinit var adapter: SongAdapter
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()
      //  fetch_song()
        setupUi()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

    }

/*    fun fetch_song(){

        val proj = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME)

        val audioCursor: Cursor? = getContentResolver()?.query(
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
                    audioList?.add(audioCursor.getString(audioIndex))
                } while (audioCursor.moveToNext())
            }
        }

        audioCursor?.close();



        */
      //  if(audioList!=null)
      //  adapter = audioList?.let { SongAdapter(it) }!!;
        //    audioView.setAdapter(adapter);

        // val adapter: ArrayAdapter<string> = ArrayAdapter<Any?>(this, R.layout.simple_list_item_1, R.id.text1, audioList)
        //  song.setAdapter(adapter) as ArrayList<String>
  //  }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
           // btn_take_picture.isEnabled=true
            Toast.makeText(this,"done",Toast.LENGTH_LONG).show()
        }
    }


    private fun setupUi() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_frag) as NavHostFragment
        navController = navHostFragment.navController

     //   val navGraph = navController.navInflater.inflate(R.navigation.)

      //  navGraph.startde
     //   navController.graph = navGraph
    }

}