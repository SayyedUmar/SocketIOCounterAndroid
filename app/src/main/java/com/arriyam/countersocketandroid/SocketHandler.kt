package com.arriyam.countersocketandroid

import android.util.Log
import io.socket.client.client.IO
import io.socket.client.client.Socket
//import io.socket.client.IO
//import io.socket.client.Socket

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("ws://10.0.2.2:7887")
        } catch (e: Exception) {
            Log.e("TAG", "URISyntaxException")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
        Log.e("TAG", "establishConnection")
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}