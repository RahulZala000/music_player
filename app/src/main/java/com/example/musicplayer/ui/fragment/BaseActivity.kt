package com.example.musicplayer.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
  //  var pDialogLoading: KAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    /*    pDialogLoading = KAlertDialog(
            this,
            KAlertDialog.PROGRESS_TYPE
        ).setTitleText(getString(R.string.please_wait))
        pDialogLoading?.setCancelable(false)
*/

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}