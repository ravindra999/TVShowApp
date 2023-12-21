package com.themoviedb.weektvshow.ui.shared

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun getNetworkStatus(): Boolean {
    val connection by connectivityState()
    return connection === NetworkState.Connected
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<NetworkState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityNetworkState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

val Context.currentConnectivityNetworkState: NetworkState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): NetworkState {
    val network = connectivityManager.activeNetwork
    val connected = connectivityManager.getNetworkCapabilities(network)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false

    return if (connected) NetworkState.Connected else NetworkState.Disconnected
}

@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = networkCallBack { connectionState -> trySend(connectionState) }

    connectivityManager.registerDefaultNetworkCallback(callback)

    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}

fun networkCallBack(callback: (NetworkState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(NetworkState.Connected)
        }

        override fun onLost(network: Network) {
            callback(NetworkState.Disconnected)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            callback(NetworkState.Disconnected)
        }
    }
}

enum class NetworkState {
    Connected, Disconnected
}