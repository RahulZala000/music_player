package com.example.musicplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductModel(

    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var title:String,
    var price:Float,
    var description:String,
    var image:String
)