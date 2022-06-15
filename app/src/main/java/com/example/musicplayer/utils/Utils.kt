package com.example.musicplayer.utils

import timber.log.Timber

object Utils {

    fun logout() {
        try {
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Timber.e(e.localizedMessage)
        }
    }
}