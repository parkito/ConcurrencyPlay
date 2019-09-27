package com.concurrency.play.corutines.tcp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Listener(port: Int) {
    private val serverSocket: ServerSocket

    private val clientSocket: Socket
    private val printWriter: PrintWriter
    private val bufferedReader: BufferedReader

    init {
        serverSocket = ServerSocket(port)
        clientSocket = serverSocket.accept()

        printWriter = PrintWriter(clientSocket.getOutputStream(), true)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }

    fun listen() {
        while (true) {
            val received = bufferedReader.readLine()
            println("Listener: received $received")
            printWriter.write("OK")
        }
    }
}

fun main(args: Array<String>) {
    val port = args[0].toInt()

    println("Listener: start listen to localhost:$port")

    val listener = Listener(port)

    listener.listen()

    println("Listener: shutting down")
}
