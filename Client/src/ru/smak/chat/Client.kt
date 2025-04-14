package ru.smak.chat

import Communicator
import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner
import kotlin.concurrent.thread

class Client(
    val host: String,
    val port: Int,
) {
    private val userScanner = Scanner(System.`in`)
    private val communicator = Communicator(Socket(host, port))

    init{
        communicator.start(::parse)

        var userInput = "-"
        thread {
            while (userInput.isNotBlank()) {
                userInput = userScanner.nextLine()
                communicator.sendMessage(userInput)
            }
        }
    }

    private fun parse(data: String){
        println(data)
    }

    fun stop() = communicator.stop()

}