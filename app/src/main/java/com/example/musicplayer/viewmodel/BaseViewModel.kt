package com.example.musicplayer.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import com.example.musicplayer.R
import com.example.musicplayer.model.BaseResponse
import com.example.musicplayer.model.ProductModel
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.utils.NetworkResult
import com.example.musicplayer.utils.Utils
import com.example.musicplayer.utils.toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import timber.log.Timber

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var context = application
    val gson: Gson = GsonBuilder().create()

    @SuppressLint("NewApi")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    fun <T> handleResponse(response: Response<T>): NetworkResult<T>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {

                val responseResult: T? = response.body()
                NetworkResult.Success(responseResult!!)
            }
            else -> {
                val data = gson.fromJson(
                    response.errorBody()!!.string(),
                    BaseResponse::class.java
                )
                Timber.e(data.message)
                try {
                    if (data.message?.contains("not logged") == true) { // static condition to logout user
                        Utils.logout()
                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        context.getString(R.string.tr)
                            .toast(context)
                        (context as Activity).finishAffinity()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                NetworkResult.Error(data.message)
            }
        }
    }

    private fun showToast(showMessage: Boolean, hasInternet: Boolean) {
        if (showMessage && !hasInternet)
            context.resources.getString(R.string.offline).toast(context)
    }
}