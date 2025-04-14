package ru.smak.chat

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Scanner
import kotlin.concurrent.thread
import Communicator

class Server(
    val port: Int = 5206
) {

    private val serverSocket: ServerSocket = ServerSocket(port)

    init{
        thread {
            while(true) {
                val socket = serverSocket.accept()
                ConnectedClient(socket)
            }
            serverSocket.close()
        }
    }
}