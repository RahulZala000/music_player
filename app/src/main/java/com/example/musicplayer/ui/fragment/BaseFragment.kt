package com.example.musicplayer.ui.fragment

import android.app.Service
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import java.security.Provider

abstract class BaseFragment: Fragment() {

    var mContext:Context?=null
    fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        mContext=context
        click()
    }

    abstract fun click()
    abstract fun setupUI()

}