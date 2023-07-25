package com.kyawzinlinn.tmdb.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar

private lateinit var dialog : ProgressDialog

fun Activity.showLoadingDialog(){
    dialog = ProgressDialog(this)
    dialog.setMessage("Loading...")
    dialog.setCancelable(false)
    dialog.show()
}

fun Activity.dismissLoadingDialog(){
    if (dialog != null) dialog.dismiss()
}

fun Activity.showToast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Activity.showErrorSnackBar(message: String, onRetry:() -> Unit){
    val snackBar = Snackbar.make(window.decorView,message, Snackbar.LENGTH_INDEFINITE)
    snackBar.setAction("retry",object : View.OnClickListener{
        override fun onClick(p0: View?) {
            onRetry()
        }
    })
    snackBar.show()
}

fun ViewGroup.setUpLayoutTransition(){
    TransitionManager.beginDelayedTransition(this)
}