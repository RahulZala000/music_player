package com.example.musicplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    private var backPressedTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupUi()

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