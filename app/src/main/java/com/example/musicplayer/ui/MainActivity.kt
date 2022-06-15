package com.example.musicplayer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.musicplayer.R
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.ui.fragment.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()
        setupUi()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

        }
        if(requestCode==200 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

        }
    }

    private fun setupUi() {

        var s=intent.getStringExtra("Notify")
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_frag) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

      if(s=="Notify")
          navGraph.startDestination=R.id.dashboardFragment
        else
            navGraph.startDestination=R.id.spalshFragment
           navController.graph = navGraph
    }

    override fun onResume() {
        super.onResume()


    }

}