package com.example.musicplayer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicplayer.databinding.FragmentSpalshBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpalshFragment : BaseFragment() {

    private var _binding: FragmentSpalshBinding? = null
    private val binding: FragmentSpalshBinding
        get() = _binding!!

    private val activityScope = CoroutineScope(Dispatchers.Main)
    val time = 2000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpalshBinding.inflate(layoutInflater, container, false)

        goToNextScreen()
        return binding.root
    }

    override fun setupUI() {

    }

    private fun goToNextScreen() {
        activityScope.launch {
            delay(time)
              navigateTo(SpalshFragmentDirections.actionSpalshFragmentToDashboardFragment())
        }
    }


}