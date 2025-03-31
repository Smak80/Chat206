package ru.smak.chat

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Scanner

class Server(
    val port: Int = 5206
) {

    private val serverSocket: ServerSocket = ServerSocket(port)

    init{
        val socket = serverSocket.accept()
        val scanner = Scanner(socket.getInputStream())
        val data = scanner.nextLine()
        println("Клиент прислал: $data")
        val writer = PrintWriter(socket.getOutputStream())
        writer.println("Ваше сообщение \"$data\" получено.")
        writer.flush()
        socket.close()
        serverSocket.close()
    }
}