package com.example.wings.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wings.R
import com.google.android.material.snackbar.Snackbar
import dmax.dialog.SpotsDialog
import okhttp3.MediaType
import okhttp3.RequestBody


/**
 * Created by Sumit R@thod on 3/26/2018.
 */

object Const {

    fun showSnackBar(activity: AppCompatActivity, message: String) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showDialog(context: Context, message: String): android.app.AlertDialog? {
        return SpotsDialog.Builder()
            .setContext(context)
            .setCancelable(true)
            .setMessage(message)
            .setTheme(R.style.CustomSpot)
            .build()
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun noInternetConnection(context: Context) {
        try {
            val alert = AlertDialog.Builder(context)
            alert.setTitle("No Internet Connection!")
            alert.setMessage("You are offline please check your internet connection!")
            alert.setPositiveButton("OK", null)
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getRBofText(text: String): RequestBody {
        Log.e(TAG, "getRBofText: $text")
        return RequestBody.create(MediaType.parse("text/plain"), text)
    }

    fun hideKeyboard(activity: AppCompatActivity) {
        try {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e)
        }
    }


}

