package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner
import kotlin.concurrent.thread

class Client(
    val host: String,
    val port: Int,
) {
    private val socket: Socket = Socket(host, port)
    private var isRunning = true
    private val scanner = Scanner(socket.getInputStream())
    private val writer = PrintWriter(socket.getOutputStream())

    private val userScanner = Scanner(System.`in`)

    init{
        startMessageAccepting()

        var userInput = "-"
        thread {
            while (userInput.isNotBlank()) {
                userInput = userScanner.nextLine()
                sendMessage(userInput)
            }
        }
    }

    private fun startMessageAccepting(){
        thread {
            while(isRunning){
                val data = scanner.nextLine()
                parse(data)
            }
            socket.close()
        }
    }

    private fun parse(data: String){
        println("От сервера пришло: $data")
    }

    fun stop(){
        isRunning = false
    }

    fun sendMessage(message: String){
        writer.println(message)
        writer.flush()
    }
}