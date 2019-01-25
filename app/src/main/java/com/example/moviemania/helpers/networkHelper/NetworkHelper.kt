package com.example.moviemania.helpers.networkHelper

import android.content.Context
import android.net.ConnectivityManager


class NetworkHelper(private val context: Context): NetworkHelperI {

    override fun isConncected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}