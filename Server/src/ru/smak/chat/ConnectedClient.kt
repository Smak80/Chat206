package ru.smak.chat

import Communicator
import kotlinx.coroutines.*
import java.net.Socket
import java.nio.channels.AsynchronousSocketChannel

class ConnectedClient(client: AsynchronousSocketChannel) {
    private val communicator = Communicator(client)
    private var userName: String? = null
    private val clientScope = CoroutineScope(Dispatchers.IO)

    init {
        communicator.start(::parse)
        connectedClients.add(this)
    }

    private fun parse(data: String){
        sendToAll(data, false)
    }

    fun stop() = communicator.stop()

    private fun sendToAll(data: String, echo: Boolean = true){
        connectedClients.forEach {
            if (echo || it != this) clientScope.launch{it.communicator.sendMessage(data)}
        }
    }

    companion object{
        private val connectedClients = mutableListOf<ConnectedClient>()
    }

}