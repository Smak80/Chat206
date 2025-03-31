package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner

class Client(
    val host: String,
    val port: Int,
) {
    private val socket: Socket = Socket(host, port)

    init{
        val writer = PrintWriter(socket.getOutputStream())
        writer.println("Привеееееет!!!!")
        writer.flush()
        val scanner = Scanner(socket.getInputStream())
        val serverData = scanner.nextLine()
        println(serverData)
        socket.close()
    }
}