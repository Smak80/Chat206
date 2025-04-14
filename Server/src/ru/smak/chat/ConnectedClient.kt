package ru.smak.chat

import Communicator
import java.net.Socket

class ConnectedClient(val client: Socket) {
    private val communicator = Communicator(client)

    init {
        communicator.start(::parse)
    }

    private fun parse(data: String){
        println("Клиент прислал: $data")
        communicator.sendMessage(data)
    }

    fun stop() = communicator.stop()

}