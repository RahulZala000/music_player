package com.example.musicplayer.utils

import android.content.Context
import android.widget.Toast

fun Any?.toast(context: Context, isShort: Boolean = false): Toast {
    return Toast.makeText(
        context,
        this.toString(),
        if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    ).apply { show() }
}