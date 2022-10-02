package com.arriyam.countersocketandroid

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.client.Socket
//import io.socket.client.Socket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {

    private lateinit var mWebSocketClient: WebSocketClient;
    lateinit var countTextView: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The following lines connects the Android app to the server.
        SocketHandler.setSocket()
        SocketHandler.getSocket().on(Socket.EVENT_CONNECT, { (data, ack) ->
            Log.d("TAG", "EVENT_CONNECT")
        })
        SocketHandler.establishConnection()

        val counterBtn = findViewById<Button>(R.id.counterBtn)
        countTextView = findViewById<TextView>(R.id.countTextView)

        val mSocket = SocketHandler.getSocket()

        counterBtn.setOnClickListener{
            mSocket.emit("counter")
            try {
                mWebSocketClient.send("counter")
            } catch (e: Exception) {e.printStackTrace()}

        }

        mSocket.on("counter") { args ->
            if (args[0] != null) {
                val counter = args[0] as Int
                runOnUiThread {
                    countTextView.text = counter.toString()
                }
            }
        }

//        connectWebSocket()
    }

    override fun onResume() {
        super.onResume()
        if (SocketHandler.getSocket().connected()){
            Log.d("TAG", "EVENT_CONNECTED")
        } else {
            Log.d("TAG", "EVENT_DISCONNECTED")
        }
    }


    private fun connectWebSocket() {
        val uri: URI
        try {
            uri = URI("ws://10.0.2.2:7887")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake?) {
                Log.i("Websocket", "Opened")
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL)

            }

            override fun onMessage(s: String) {
                runOnUiThread {
                    countTextView.text = s
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("Websocket", "Closed $s")
            }

            override fun onError(e: Exception) {
                Log.i("Websocket", "Error " + e.message)
            }
        }
        
        mWebSocketClient.connect()
    }

    fun sendMessage(view: View?) {
        mWebSocketClient.send("")
    }
}