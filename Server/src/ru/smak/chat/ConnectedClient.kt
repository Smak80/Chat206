package ru.smak.chat

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class ConnectedClient(val client: Socket) {

    private var isRunning = true
    private val scanner = Scanner(client.getInputStream())
    private val writer = PrintWriter(client.getOutputStream())

    init {
        startMessageAccepting()
    }

    private fun startMessageAccepting(){
        thread {
            while(isRunning){
                val data = scanner.nextLine()
                parse(data)
            }
            client.close()
        }
    }

    fun sendMessage(message: String){
        writer.println("Ваше сообщение \"$message\" получено.")
        writer.flush()
    }

    private fun parse(data: String){
        println("Клиент прислал: $data")
        sendMessage(data)
    }

    fun stop(){
        isRunning = false
    }
}