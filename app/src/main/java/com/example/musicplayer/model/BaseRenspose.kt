package com.example.musicplayer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse : Parcelable {
    val message: String? = null
    val success: Boolean? = null
}