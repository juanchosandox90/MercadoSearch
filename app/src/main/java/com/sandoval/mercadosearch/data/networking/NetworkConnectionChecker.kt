package com.sandoval.mercadosearch.data.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkConnectionChecker(private val context: Context) {

    fun isAvailable(): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        return if (connectivityManager is ConnectivityManager) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                this.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || this.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            } ?: false
        } else {
            false
        }
    }
}