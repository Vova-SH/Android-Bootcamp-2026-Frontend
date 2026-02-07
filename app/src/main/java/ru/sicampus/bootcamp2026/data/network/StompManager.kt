package ru.sicampus.bootcamp2026.data.network

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import ru.sicampus.bootcamp2026.data.auth.TokenStorage

class StompManager(private val tokenStorage: TokenStorage) : WebSocketListener() {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _updates = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val updates = _updates.asSharedFlow()

    fun connect() {
        val userId = tokenStorage.getUserId()
        if (userId == -1L) return

        val token = tokenStorage.getToken() ?: return

        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws-android")
            .header("Authorization", "Basic $token")
            .build()

        webSocket = client.newWebSocket(request, this)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("STOMP", "Connection open")
        val connectFrame = "CONNECT\naccept-version:1.1,1.0\nheart-beat:10000,10000\n\n\u0000"
        webSocket.send(connectFrame)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("STOMP", "Received: $text")

        if (text.startsWith("CONNECTED")) {
            val userId = tokenStorage.getUserId()
            val subscribeFrame = "SUBSCRIBE\nid:sub-0\ndestination:/topic/invites/$userId\n\n\u0000"
            webSocket.send(subscribeFrame)
        } else if (text.startsWith("MESSAGE")) {
            GlobalScope.launch {
                _updates.emit(Unit)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("STOMP", "Error", t)
    }

    fun disconnect() {
        webSocket?.close(1000, "Logout")
    }
}