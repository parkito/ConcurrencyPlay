package com.concurrency.play.corutines.tcp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Publisher(port: Int) {

    private val clientSocket: Socket
    private val printWriter: PrintWriter
    private val bufferedReader: BufferedReader

    init {
        clientSocket = Socket("localhost", port)

        printWriter = PrintWriter(clientSocket.getOutputStream(), true)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }

    fun publish(message: String) {
        printWriter.println(message)

        val response = bufferedReader.readLine()
        println("Publisher: received $response")

        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
    }
}

fun main(args: Array<String>) {
    val port = args[0].toInt()
    val message = args[1]

    println("Publisher: publishing $message on localhost:$port")

    val publisher = Publisher(port)
    publisher.publish(message)

    println("Publisher: shutting down")
}